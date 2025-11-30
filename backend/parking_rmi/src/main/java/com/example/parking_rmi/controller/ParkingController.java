package com.example.parking_rmi.controller;

import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.service.ParkingServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/parking")

public class ParkingController {

    @Autowired
    private ParkingServ parkingServ;

    // 1. Récupérer tous les parkings (Pour la HomePage et la Map)
    @GetMapping("/lots")
    public ResponseEntity<List<ParkingLot>> getAllParkingLots() {
        return ResponseEntity.ok(parkingServ.getAllParkingLots());
    }

    // 2. Récupérer un parking par ID
    @GetMapping("/{id}")
    public ResponseEntity<ParkingLot> getParkingById(@PathVariable Long id) {
        ParkingLot parking = parkingServ.getParkingLotById(id);
        if (parking != null) {
            return ResponseEntity.ok(parking);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Récupérer les places d'un parking (Pour le modal ParkingDetails)
    @GetMapping("/{id}/spots")
    public ResponseEntity<List<ParkingSpot>> getParkingSpots(@PathVariable Long id) {
        return ResponseEntity.ok(parkingServ.getAvailableSpots(id));
    }
}
