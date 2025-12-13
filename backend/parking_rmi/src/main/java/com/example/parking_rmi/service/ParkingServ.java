package com.example.parking_rmi.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.dto.ReservationDTO;

@Service
public class ParkingServ {

    @Autowired
    private ParkingService parkingService; // This is the RMI Proxy from RmiConfig

    public List<ParkingLotDTO> getAllParkingLots() {
        try {
            return parkingService.getAllParkingLots();
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list or throw custom runtime exception
        }
    }

    public ParkingLotDTO getParkingLotById(Long id) {
        try {
            return parkingService.getParkingLotById(id);
        } catch (RemoteException e) {
            throw new RuntimeException("RMI Error: Could not fetch parking lot " + id, e);
        }
    }

    public List<ParkingSpotDTO> getAvailableSpots(Long parkingId) {
        try {
            return parkingService.getAvailableSpots(parkingId);
        } catch (RemoteException e) {
            throw new RuntimeException("RMI Error fetching spots", e);
        }
    }

    public ReservationDTO createReservation(ReservationDTO reservation) {
        try {
            return parkingService.createReservation(reservation);
        } catch (RemoteException e) {
            throw new RuntimeException("RMI Error creating reservation", e);
        }
    }

    
    //Cancel
    public boolean cancelReservation(Long id) {
        try {
            return parkingService.cancelReservation(id);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Get By Email
    public List<ReservationDTO> getReservationsByUserEmail(String email) {
    try {
        // Hna kan3iyto l RMI Server
        return parkingService.getReservationsByUserEmail(email);
    } catch (Exception e) {         e.printStackTrace();
        return new ArrayList<>(); // Rjje3 list khawya ila kan mochkil
    }
}
}