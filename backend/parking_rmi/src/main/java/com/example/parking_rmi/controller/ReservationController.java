package com.example.parking_rmi.controller;

import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.service.ParkingServ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ParkingServ parkingServ;

    /**
     * 1. Créer une réservation
     * Accepts a DTO, calls the RMI service, and returns the created DTO.
     */
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO created = parkingServ.createReservation(reservationDTO);
            if (created == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error here if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 2. Historique utilisateur (Mes Réservations)
     * Returns a list of DTOs.
     */
   
}