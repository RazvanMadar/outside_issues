package com.license.outside_issues.service.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.license.outside_issues.controller.api.WebSocketController;
import com.license.outside_issues.dto.WebSocketMessageUpdateDTO;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.model.Address;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.repository.IssueRepository;
import com.license.outside_issues.service.citizen.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArduinoSerialListener implements SerialPortDataListener {
    @Autowired
    private final IssueRepository issueRepository;
    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private CitizenService citizenService;
    private String buffer = "";
    private final Gson gson = new Gson();

    public ArduinoSerialListener(IssueRepository issueRepository) {
        super();
        this.issueRepository = issueRepository;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] content = new byte[event.getSerialPort().bytesAvailable()];
        event.getSerialPort().readBytes(content, content.length);

        String s = new String(content);
        buffer = buffer.concat(s);

        handleReceivedData();
    }

    private void handleReceivedData() {
        if (buffer.indexOf(']') != -1) {
            JsonElement jsonIssueElement = gson.fromJson(buffer, JsonElement.class);
            JsonArray allReportedIssues = jsonIssueElement.getAsJsonArray();
            for (int i = 0; i < allReportedIssues.size(); i++) {
                JsonObject issue = allReportedIssues.get(i).getAsJsonObject();
                addIssueIfNecessary(issue);
            }
            buffer = "";
        }
    }

    private void addIssueIfNecessary(JsonObject issue) {
        Gson gson = new Gson();
        JsonObject issueJson = gson.fromJson(issue, JsonObject.class);
        JsonObject address = issue.getAsJsonObject().get("address").getAsJsonObject();
        Double addressLat = address.get("lat").getAsDouble();
        Double addressLng = address.get("lng").getAsDouble();
        String type = issueJson.get("type").getAsString();
        String state = issueJson.get("state").getAsString();
        Address issueAddress = new Address(addressLat, addressLng);
        int likesNumber = issueJson.get("likesNumber").getAsInt();
        int dislikesNumber = issueJson.get("dislikesNumber").getAsInt();
        boolean hasLocation = issueJson.get("hasLocation").getAsBoolean();
        int value = issueJson.get("value").getAsInt();
        if (isAvailableIssue(type, value)) {
            Issue newIssue = new Issue();
            newIssue.setType(IssueType.valueOf(type));
            newIssue.setState(IssueState.valueOf(state));
            newIssue.setLikesNumber(likesNumber);
            newIssue.setDislikesNumber(dislikesNumber);
            newIssue.setHasLocation(hasLocation);
            newIssue.setAddress(issueAddress);
            newIssue.setReportedDate(LocalDate.now());
            newIssue.setDescription("");
            String actualLocation = computeActualLocation(addressLat, addressLng);
            newIssue.setActualLocation(actualLocation);
            issueRepository.save(newIssue);
            sendMessagesViaWebSocketOnUpdate(citizenService.findAllValidEmails());
        }
    }

    private boolean isAvailableIssue(String type, int value) {
        if (IssueType.PUBLIC_DISORDER.name().equals(type)) {
            return value > 100;
        } else if (IssueType.LIGHTNING.name().equals(type)) {
            return value < 5000;
        } else {
            return value > 8;
        }
    }

    private String computeActualLocation(Double addressLat, Double addressLng) {
        try {
            String path = "https://nominatim.openstreetmap.org/reverse?lat=" + addressLat + "&lon=" + addressLng + "&format=json";
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            String actualAddress = "";
            boolean moreThanOneWord = false;
            JsonElement element = gson.fromJson(String.valueOf(content), JsonElement.class);
            JsonObject obj = element.getAsJsonObject().get("address").getAsJsonObject();
            final JsonElement road = obj.get("road");
            if (road != null) {
                actualAddress += road.getAsString();
                moreThanOneWord = true;
            }
            final JsonElement houseNumber = obj.get("house_number");
            if (houseNumber != null) {
                if (moreThanOneWord) {
                    actualAddress += ", ";
                }
                actualAddress += houseNumber.getAsString();
                moreThanOneWord = true;
            }
            final JsonElement suburb = obj.get("suburb");
            if (suburb != null) {
                if (moreThanOneWord) {
                    actualAddress += ", ";
                }
                actualAddress += suburb.getAsString();
            }
            final JsonElement village = obj.get("village");
            if (village != null) {
                if (moreThanOneWord) {
                    actualAddress += ", ";
                }
                actualAddress += village.getAsString();
            }

            return actualAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sendMessagesViaWebSocketOnUpdate(List<String> emails) {
        final List<WebSocketMessageUpdateDTO> webSocketMessageUpdates = emails.stream()
                .map(WebSocketMessageUpdateDTO::new)
                .collect(Collectors.toList());
        webSocketController.sendUpdate(webSocketMessageUpdates);
    }
}
