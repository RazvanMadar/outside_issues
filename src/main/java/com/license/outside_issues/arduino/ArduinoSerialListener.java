package com.license.outside_issues.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.model.Address;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ArduinoSerialListener implements SerialPortDataListener {
    @Autowired
    private final IssueRepository issueRepository;
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
                createIssueFromJson(obj);
            }

            bufferReadToString = "";
        }
    }

    private void createIssueFromJson(JsonObject json) {
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

        Issue issue = new Issue();
        issue.setType(IssueType.valueOf(type));
        issue.setState(IssueState.valueOf(state));
        issue.setLikesNumber(likesNumber);
        issue.setDislikesNumber(dislikesNumber);
        issue.setHasLocation(hasLocation);
        issue.setAddress(issueAddress);
        issue.setReportedDate(LocalDate.now());
        issue.setDescription("");
        // TODO: add actual location
        System.out.println(issue);
        issueRepository.save(issue);
    }
}
