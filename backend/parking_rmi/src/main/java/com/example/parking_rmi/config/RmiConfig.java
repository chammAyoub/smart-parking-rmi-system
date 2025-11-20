package com.example.parking_rmi.config;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.parking_rmi.Interface.ParkingService;

@Configuration
public class RmiConfig {

    @Bean
    public ParkingService parkingService() {
        try {
            System.out.println("🔌 Connecting to ClientService...");
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ParkingService service = (ParkingService) registry.lookup("parkingService");
            System.out.println("✅ ClientService connected");
            return service;
        } catch (Exception e) {
            System.err.println("❌ Failed to connect ClientService");
            throw new RuntimeException("the error in connection : "+e);
        }
    }
}
