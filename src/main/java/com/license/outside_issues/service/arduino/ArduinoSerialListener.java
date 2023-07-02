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
    private WebSocketController webSocketResource;
    @Autowired
    private CitizenService citizenService;
    private String bufferReadToString = "";
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
        byte[] buffer = new byte[event.getSerialPort().bytesAvailable()];
        event.getSerialPort().readBytes(buffer, buffer.length);

        String s = new String(buffer);
        bufferReadToString = bufferReadToString.concat(s);

        handleReceivedData();
    }

    private void handleReceivedData() {
        if (bufferReadToString.indexOf(']') != -1) {
            JsonElement element = gson.fromJson(bufferReadToString, JsonElement.class);
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                handleDataFromJson(obj);
            }

            bufferReadToString = "";
        }
    }

    private void handleDataFromJson(JsonObject json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject address = json.getAsJsonObject().get("address").getAsJsonObject();
        Double addressLat = address.get("lat").getAsDouble();
        Double addressLng = address.get("lng").getAsDouble();
        String type = jsonObject.get("type").getAsString();
        String state = jsonObject.get("state").getAsString();
        Address issueAddress = new Address(addressLat, addressLng);
        int likesNumber = jsonObject.get("likesNumber").getAsInt();
        int dislikesNumber = jsonObject.get("dislikesNumber").getAsInt();
        boolean hasLocation = jsonObject.get("hasLocation").getAsBoolean();
        int value = jsonObject.get("value").getAsInt();
        if (isAvailableIssue(type, value)) {
            Issue issue = new Issue();
            issue.setType(IssueType.valueOf(type));
            issue.setState(IssueState.valueOf(state));
            issue.setLikesNumber(likesNumber);
            issue.setDislikesNumber(dislikesNumber);
            issue.setHasLocation(hasLocation);
            issue.setAddress(issueAddress);
            issue.setReportedDate(LocalDate.now());
            issue.setDescription("");
            String actualLocation = computeActualLocation(addressLat, addressLng);
            issue.setActualLocation(actualLocation);
            issueRepository.save(issue);
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
        webSocketResource.sendUpdate(webSocketMessageUpdates);
    }
}
