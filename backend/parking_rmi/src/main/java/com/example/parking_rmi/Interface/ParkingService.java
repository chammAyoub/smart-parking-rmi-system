package com.example.parking_rmi.Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;

public interface ParkingService extends Remote {

    public ParkingLotDTO createParkingLot(ParkingLotDTO parkingLotDTO) throws RemoteException;

    public int getAvailableSpotsCount(Long parkingLotId) throws RemoteException;

    public int getTotalAvailableSpots() throws RemoteException;

    public double getOccupancyRate(Long parkingLotId) throws RemoteException;

    public List<ParkingLotDTO> getAllParkingLots() throws RemoteException;

    public ParkingLotDTO getParkingLotById(Long id) throws RemoteException;

    public List<ParkingLotDTO> getActiveParkingLots() throws RemoteException;

    public List<ParkingSpotDTO> getAllSpotsByParkingLot(Long parkingLotId) throws RemoteException;

    public List<ParkingSpotDTO> getAvailableSpots(Long parkingLotId) throws RemoteException;

    public ParkingSpotDTO getSpotById(Long spotId) throws RemoteException;

    public boolean updateSpotStatus(Long spotId, String status) throws RemoteException;

    public ReservationDTO createReservation(ReservationDTO reservationDTO) throws RemoteException;

    public ReservationDTO getReservationById(Long id) throws RemoteException;

    public List<ReservationDTO> getReservationsByUserEmail(String email) throws RemoteException;

    public boolean cancelReservation(Long id) throws RemoteException;

    public List<ReservationDTO> getReservationDTOsByParkingLot(long id) throws RemoteException;

    public boolean simulateCarExit(long spotId) throws RemoteException;

    public boolean simulateCarEntry(long spotId) throws RemoteException;

    List<ParkingSpotDTO> findByStatus(SpotStatus status);

    List<ParkingSpotDTO> findByParkingLotIdAndStatus(Long parkingLotId, SpotStatus status);

    Long countAvailableSpots(Long parkingLotId);

    Long countOccupiedSpots(Long parkingLotId);

    Double getTotalRevenueByParkingLotId(Long parkingLotId);
}