package com.license.outside_issues;

import com.license.outside_issues.arduino.ArduinoSerialCommunication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.fazecast.jSerialComm.*;

import java.util.Arrays;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class})
public class OutsideIssuesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutsideIssuesApplication.class, args);

//		SerialPort[] ports = SerialPort.getCommPorts();
//		SerialPort serialPort = ports[0];
//		serialPort.setBaudRate(9600);
//		serialPort.setParity(SerialPort.NO_PARITY);
//		serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
//		serialPort.setNumDataBits(8);
//		ArduinoSerialCommunication arduinoListener = new ArduinoSerialCommunication(serialPort);
//		arduinoListener.startListeningScheduled();
	}

}
