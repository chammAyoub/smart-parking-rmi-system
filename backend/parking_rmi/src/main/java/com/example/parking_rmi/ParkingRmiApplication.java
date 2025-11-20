package com.example.parking_rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.parking_rmi.Interface.ParkingService;

@SpringBootApplication
public class ParkingRmiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingRmiApplication.class, args);
	}

}
