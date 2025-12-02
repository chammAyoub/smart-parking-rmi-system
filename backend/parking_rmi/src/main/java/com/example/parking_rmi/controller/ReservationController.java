package com.example.parking_rmi.controller;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.service.ParkingServ;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")

public class ReservationController {

    @Autowired
    ParkingService parkingService;

    /**
     * 1. Créer une réservation
     * Accepts a DTO, calls the RMI service, and returns the created DTO.
     */
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO created = parkingService.createReservation(reservationDTO);
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
   @PostMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) throws Exception {
        boolean cancelled = parkingService.cancelReservation(id);
        if (cancelled) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<ReservationDTO>> getReservationByEmail(@PathVariable String email)throws Exception{
        List<ReservationDTO> reservationDTO = parkingService.getReservationsByUserEmail(email);
        if(reservationDTO.isEmpty()){
            return ResponseEntity.ok(List.of());
        }
        else{
            return ResponseEntity.ok(reservationDTO);
        }
    }
}