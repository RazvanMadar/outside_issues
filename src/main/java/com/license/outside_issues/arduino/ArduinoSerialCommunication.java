package com.license.outside_issues.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArduinoSerialCommunication {
    private final SerialPort serialPort;

    public ArduinoSerialCommunication(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    private void startSerialCommunicationListening() {
//        SerialPort[] ports = SerialPort.getCommPorts();
//        SerialPort serialPort = ports[0];
//
//        serialPort.setBaudRate(9600);
//        serialPort.setParity(SerialPort.NO_PARITY);
//        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
//        serialPort.setNumDataBits(8);
        if (!serialPort.openPort()) {
            System.err.println("Failed to open the serial port: " + serialPort.getSystemPortName());
            return;
        }
        System.out.println("Serial port opened: " + serialPort.getSystemPortName());

        if (serialPort.openPort()) {
            byte[] readBuffer = new byte[serialPort.bytesAvailable()];
            int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
            if (numRead > 0) {
                try {
                    // Deserialize the object
                    ByteArrayInputStream byteStream = new ByteArrayInputStream(readBuffer);
                    ObjectInputStream objStream = new ObjectInputStream(byteStream);
                    Object obj = objStream.readObject();
                    System.out.println("Received object: id=" + obj);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
//                String receivedData = new String(readBuffer, 0, numRead);
//                System.out.println("Received data: " + receivedData);
            }
        }
//        serialPort.closePort();
    }

    private void startSerialCommunicationSending() {
        if (!serialPort.openPort()) {
            System.err.println("Failed to open the serial port.");
            return;
        }
        String message = "Hello Arduino!";
        byte[] bytesToSend = message.getBytes();
        int bytesWritten = serialPort.writeBytes(bytesToSend, bytesToSend.length);
        if (bytesWritten == bytesToSend.length) {
            System.out.println("Sent message to Arduino: " + message);
        } else {
            System.err.println("Failed to send message to Arduino.");
        }
        serialPort.closePort();
    }

    public void startListeningScheduled() {
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.scheduleAtFixedRate(this::startSerialCommunicationListening, 0, 10, TimeUnit.SECONDS);
//        scheduler.scheduleAtFixedRate(this::startSerialCommunicationSending, 0, 5, TimeUnit.SECONDS);
    }
}
