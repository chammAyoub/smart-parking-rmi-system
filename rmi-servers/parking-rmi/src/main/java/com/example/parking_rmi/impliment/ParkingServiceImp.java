package com.example.parking_rmi.impliment;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.*;
import com.example.parking_rmi.service.ParkingLotService;
import com.example.parking_rmi.service.ParkingSpotService;
import com.example.parking_rmi.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

@Slf4j
@Component
public class ParkingServiceImp extends UnicastRemoteObject implements ParkingService {

    private final ParkingLotService parkingLotService;
    private final ParkingSpotService parkingSpotService;
    private final ReservationService reservationService;

    // Constructor Injection of Services
    public ParkingServiceImp(ParkingLotService s1, ParkingSpotService s2, ReservationService s3)
            throws RemoteException {
        super();
        this.parkingLotService = s1;
        this.parkingSpotService = s2;
        this.reservationService = s3;
    }

    // ==================== PARKING LOTS ====================
    @Override
    public ParkingLotDTO createParkingLot(ParkingLotDTO dto) throws RemoteException {
        return parkingLotService.createParkingLot(dto);
    }

    @Override
    public List<ParkingLotDTO> getAllParkingLots() throws RemoteException {
        return parkingLotService.getAllParkingLots();
    }

    @Override
    public ParkingLotDTO getParkingLotById(Long id) throws RemoteException {
        return parkingLotService.getParkingLotById(id);
    }

    @Override
    public List<ParkingLotDTO> getActiveParkingLots() throws RemoteException {
        return parkingLotService.getActiveParkingLots();
    }

    @Override
    public int getTotalAvailableSpots() throws RemoteException {
        return parkingLotService.getTotalAvailableSpots();
    }

    // ==================== SPOTS ====================

    @Override
    public boolean simulateCarExit(long spotId) throws RemoteException {
        return parkingSpotService.simulateCarExit(spotId);
    }

    @Override
    public boolean simulateCarEntry(long spotId) throws RemoteException {
        return parkingSpotService.simulateCarEntry(spotId);
    }

    @Override
    public List<ParkingSpotDTO> getAllSpotsByParkingLot(Long parkingLotId) throws RemoteException {
        return parkingSpotService.getAllSpotsByParkingLot(parkingLotId);
    }

    @Override
    public List<ParkingSpotDTO> getAvailableSpots(Long parkingLotId) throws RemoteException {
        return parkingSpotService.getAvailableSpots(parkingLotId);
    }

    @Override
    public ParkingSpotDTO getSpotById(Long spotId) throws RemoteException {
        return parkingSpotService.getSpotById(spotId);
    }

    @Override
    public boolean updateSpotStatus(Long spotId, String status) throws RemoteException {
        return parkingSpotService.updateSpotStatus(spotId, status);
    }

    @Override
    public int getAvailableSpotsCount(Long parkingLotId) throws RemoteException {
        return parkingSpotService.getAvailableSpotsCount(parkingLotId);
    }

    @Override
    public double getOccupancyRate(Long parkingLotId) throws RemoteException {
        return parkingSpotService.getOccupancyRate(parkingLotId);
    }

    // ==================== RESERVATIONS ====================

    @Override
    public ReservationDTO createReservation(ReservationDTO dto) throws RemoteException {
        try {
            return reservationService.createReservation(dto);
        } catch (Exception e) {
            log.error("Error creating reservation", e);
            throw new RemoteException("Reservation failed: " + e.getMessage());
        }
    }

    @Override
    public ReservationDTO getReservationById(Long id) throws RemoteException {
        return reservationService.getReservationById(id);
    }

    @Override
    public List<ReservationDTO> getReservationsByUserEmail(String email) throws RemoteException {
        return reservationService.getReservationsByUserEmail(email);
    }

    @Override
    public List<ReservationDTO> getReservationDTOsByParkingLot(long id) throws RemoteException {
        return reservationService.getReservationDTOsByParkingLot(id);
    }

    @Override
    public boolean cancelReservation(Long id) throws RemoteException {
        return reservationService.cancelReservation(id);
    }

}