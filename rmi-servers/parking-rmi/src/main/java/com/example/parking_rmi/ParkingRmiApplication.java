package com.example.parking_rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.impliment.ParkingServiceImp;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootApplication
public class ParkingRmiApplication {

	public static void main(String[] args) {
		System.out.println("🚀 Starting Spring Boot Application...\n");
        ApplicationContext context = SpringApplication.run(ParkingRmiApplication.class, args);
        
        // Wait a moment for JPA to fully initialize
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		ParkingService parkingService;
		try {
			parkingService=context.getBean(ParkingServiceImp.class);
			if (parkingService==null) {
				throw new RuntimeException("ERROR IN THE BEANS");
			}
			Registry registry = LocateRegistry.createRegistry(1099);
			
			registry.bind("parkingService", parkingService);
			System.out.println("-------------------------------------------");
			System.out.println("                SERVER RUNNING             ");
			System.out.println("-------------------------------------------");
		} catch (Exception e) {
			log.info("the server rmi shutdown !!");
		}
	}

}
