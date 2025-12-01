package com.example.parking_rmi.Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.Reservation;

public interface ParkingService extends Remote {
     public int getAvailableSpotsCount(Long parkingLotId) throws RemoteException;

    public List<ParkingLot> getAllParkingLots() throws RemoteException;

    public ParkingLot getParkingLotById(Long id) throws RemoteException;

    public List<ParkingLot> getActiveParkingLots() throws RemoteException;

    public List<ParkingSpot> getAllSpotsByParkingLot(Long parkingLotId) throws RemoteException;

    public List<ParkingSpot> getAvailableSpots(Long parkingLotId) throws RemoteException;

    public ParkingSpot getSpotById(Long spotId) throws RemoteException;

    public boolean updateSpotStatus(Long spotId, String status) throws RemoteException;

    public Reservation createReservation(Reservation reservationDTO) throws RemoteException;

    public Reservation getReservationById(Long id) throws RemoteException;

    public List<Reservation> getReservationsByUserEmail(String email) throws RemoteException;

    public boolean cancelReservation(Long id) throws RemoteException;

    public int getTotalAvailableSpots() throws RemoteException;

    public double getOccupancyRate(Long parkingLotId) throws RemoteException;
}
