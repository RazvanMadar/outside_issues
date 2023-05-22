package com.license.outside_issues;

import com.fazecast.jSerialComm.SerialPort;
import com.license.outside_issues.arduino.ArduinoSerialListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class OutsideIssuesApplication {
	private final ArduinoSerialListener arduinoSerialListener;

	public OutsideIssuesApplication(ArduinoSerialListener arduinoSerialListener) {
		this.arduinoSerialListener = arduinoSerialListener;
	}

	public static void main(String[] args) {
		SpringApplication.run(OutsideIssuesApplication.class, args);
	}

	@PostConstruct
	public void setup() {
		SerialPort[] ports = SerialPort.getCommPorts();
		if (ports.length > 0) {
			SerialPort serialPort = ports[0];
			serialPort.setBaudRate(9600);
			serialPort.setParity(SerialPort.NO_PARITY);
			serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
			serialPort.setNumDataBits(8);

			if (serialPort.openPort()) {
				System.out.println("Port is open");
				serialPort.addDataListener(arduinoSerialListener);
			}
		}
	}
}
