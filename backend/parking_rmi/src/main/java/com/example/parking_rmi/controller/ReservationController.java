package com.example.parking_rmi.controller;

import com.example.parking_rmi.model.Reservation;
import com.example.parking_rmi.service.ParkingServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ParkingServ parkingServ;

    // 1. Créer une réservation (Quand tu cliques sur "Confirmer" dans le modal)
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation created = parkingServ.createReservation(reservation);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur: " + e.getMessage());
        }
    }

    // 2. Historique utilisateur (Mes Réservations)
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable String email) {
        return ResponseEntity.ok(parkingServ.getUserReservations(email));
    }

    // 3. Annuler une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        boolean cancelled = parkingServ.cancelReservation(id);
        if (cancelled) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
