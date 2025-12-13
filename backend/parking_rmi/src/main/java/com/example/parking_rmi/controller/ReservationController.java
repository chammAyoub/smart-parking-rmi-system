package com.example.parking_rmi.controller;

import com.example.parking_rmi.service.ParkingServ; // ✅ Import Wrapper
import com.example.parking_rmi.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ParkingServ clientService; // ✅ Sta3ml Wrapper li fih try-catch

    /**
     * 1. Créer une réservation
     */
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        // Validation simple
        if (reservationDTO.getParkingLotId() == null || reservationDTO.getParkingSpotId() == null) {
             return ResponseEntity.badRequest().build();
        }

        ReservationDTO created = clientService.createReservation(reservationDTO);
        
        if (created != null) {
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } else {
            // Ila rje3 null, ya3ni blassa 3amra ola parking makaynch
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
        }
    }

    /**
     * 2. Annuler une réservation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        boolean isCancelled = clientService.cancelReservation(id);
        if (isCancelled) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }

    @GetMapping("/user/{email}") 
public ResponseEntity<List<ReservationDTO>> getReservationByEmail(@PathVariable String email) {
    // ✅ DABA S7I7A: Kan3iyto l service
    List<ReservationDTO> reservations = clientService.getReservationsByUserEmail(email);
    return ResponseEntity.ok(reservations);
}
}