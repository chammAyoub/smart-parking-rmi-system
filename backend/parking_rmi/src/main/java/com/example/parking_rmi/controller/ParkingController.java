package com.example.parking_rmi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.parking_rmi.service.ParkingServ;
import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.dto.ReservationDTO;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingServ clientService;

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    public ResponseEntity<List<ParkingLotDTO>> getAllParkingLots() {
        return ResponseEntity.ok(clientService.getAllParkingLots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLotDTO> getParkingLotById(@PathVariable Long id) {
        ParkingLotDTO lot = clientService.getParkingLotById(id);
        if (lot == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(lot);
    }

    @GetMapping("/{id}/spots/available")
    public ResponseEntity<List<ParkingSpotDTO>> getAvailableSpots(@PathVariable Long id)throws Exception {
        return ResponseEntity.ok(parkingService.getAvailableSpots(id));
    }

    @PostMapping("/reservation")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservation) {
        ReservationDTO created = clientService.createReservation(reservation);
        if (created == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(created);
    }
}