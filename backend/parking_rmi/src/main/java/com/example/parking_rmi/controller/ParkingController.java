package com.example.parking_rmi.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.parking_rmi.service.ParkingServ;
import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingServ clientService;

    @Autowired
    private ParkingService parkingService;

    @GetMapping("/lots")
    public ResponseEntity<List<ParkingLotDTO>> getAllParkingLots() {
        return ResponseEntity.ok(clientService.getAllParkingLots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLotDTO> getParkingLotById(@PathVariable Long id) throws Exception {
        ParkingLotDTO lot = parkingService.getParkingLotById(id);
        List <ParkingSpotDTO> parkingSpotDTOs = parkingService.getAllSpotsByParkingLot(id);
        List<ReservationDTO> reservationDTOs = parkingService.getReservationDTOsByParkingLot(id);
        lot.setSpots(parkingSpotDTOs);
        lot.setReservations(reservationDTOs);
        return ResponseEntity.ok(lot);
    }

    @GetMapping("/{id}/spots/available")
    public ResponseEntity<List<ParkingSpotDTO>> getAvailableSpots(@PathVariable Long id)throws Exception {
        List<ParkingSpotDTO> parkingSpotDTOs =parkingService.getAllSpotsByParkingLot(id);
        List<ParkingSpotDTO> resulta = new ArrayList<>();
        for (ParkingSpotDTO parkingSpotDTO : parkingSpotDTOs) {
            if (parkingSpotDTO.getStatus().equals(SpotStatus.AVAILABLE.name())) {
                resulta.add(parkingSpotDTO);
            }
        }
        return ResponseEntity.ok(resulta);
    }
    

    // Zid had l method f ParkingController.java

    @GetMapping("/{id}/spots") // <--- Hada howa l path li kan na9s
    public ResponseEntity<List<ParkingSpotDTO>> getSpotsByParkingId(@PathVariable Long id) throws Exception {
        // Hna kansta3mlo nafss service li déjà knti dayr f getParkingLotById
        return ResponseEntity.ok(parkingService.getAllSpotsByParkingLot(id));
    }

    
}