package com.example.parking_rmi.service;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.Reservation;

@Service
public class ParkingServ {
    @Autowired
    ParkingService parkingService;
    public List<ParkingLot> getAllParkingLots() {
        try {
            return parkingService.getAllParkingLots();
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur RMI: Impossible de récupérer les parkings", e);
        }
    }

    public ParkingLot getParkingLotById(Long id) {
        try {
            return parkingService.getParkingLotById(id);
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur RMI: Impossible de récupérer le parking " + id, e);
        }
    }


    public List<ParkingSpot> getAvailableSpots(Long parkingId) {
        try {
            return parkingService.getAvailableSpots(parkingId);
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur RMI: Impossible de récupérer les places", e);
        }
    }


    public Reservation createReservation(Reservation reservation) {
        try {
            return parkingService.createReservation(reservation);
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur RMI: Echec de la réservation", e);
        }
    }

    public List<Reservation> getUserReservations(String email) {
        try {
            return parkingService.getReservationsByUserEmail(email);
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur RMI: Impossible de récupérer l'historique", e);
        }
    }

    public boolean cancelReservation(Long id) {
        try {
            return parkingService.cancelReservation(id);
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur RMI: Impossible d'annuler la réservation", e);
        }
    }

    
}
