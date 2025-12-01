package com.example.parking_rmi.test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.example.parking_rmi.Interface.ParkingService;

public class Test {
    public static void main(String[] args) {
         try {
            System.out.println("🔌 Connecting to ClientService...");
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ParkingService service = (ParkingService) registry.lookup("parkingService");
            System.out.println("✅ ClientService connected");
            System.out.println(service.getAllSpotsByParkingLot(Long.valueOf(1)));
        } catch (Exception e) {
            System.err.println("❌ Failed to connect ClientService");
            throw new RuntimeException("the error in connection : "+e);
        }
    }
}
