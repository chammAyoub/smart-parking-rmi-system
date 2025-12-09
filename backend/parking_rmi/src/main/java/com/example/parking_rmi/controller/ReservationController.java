package com.example.parking_rmi.controller;

import com.example.parking_rmi.Interface.ParkingService;
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
    ParkingService parkingService;

    /**
     * 1. Créer une réservation
     * Accepts a DTO, calls the RMI service, and returns the created DTO.
     */
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            System.out.println(reservationDTO.toString());
            ReservationDTO created = parkingService.createReservation(reservationDTO);
            System.out.println(created);
            if (created == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            System.out.println("valide");
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            System.out.println("test valide");
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        System.out.println("test no valide");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<ReservationDTO>> getReservationByEmail(@PathVariable String email) throws Exception {
        List<ReservationDTO> reservationDTO = parkingService.getReservationsByUserEmail(email);
        if (reservationDTO.isEmpty()) {

            return ResponseEntity.ok(List.of());
        } else {
            System.out.println(reservationDTO);

            return ResponseEntity.ok(reservationDTO);
        }
    }
}