package com.example.parking_rmi.impliment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.Repository.ParkingLotRepository;
import com.example.parking_rmi.Repository.ParkingSpotRepository;
import com.example.parking_rmi.Repository.ReservationRepository;
import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.Reservation;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ParkingServiceImp extends UnicastRemoteObject implements ParkingService {

    public final ParkingLotRepository parkingLotRepository;

    public final ParkingSpotRepository parkingSpotRepository;

    public final ReservationRepository reservationRepository;

    public ParkingServiceImp(ParkingLotRepository parkingLotRepository,
                             ParkingSpotRepository parkingSpotRepository,
                             ReservationRepository reservationRepository
                             ) throws RemoteException {
        super();
        this.parkingLotRepository=parkingLotRepository;
        this.parkingSpotRepository=parkingSpotRepository;
        this.reservationRepository=reservationRepository;
    }

    @Override
    public int getAvailableSpotsCount(Long parkingLotId) throws RemoteException {
        Optional<ParkingLot> optional = parkingLotRepository.findById(parkingLotId);
        if(optional.isPresent()){
            return parkingLotRepository.getTotalAvailableSpots().intValue();
        }
        else{
            return 0;
        }
    }

    @Override
    public List<ParkingLot> getAllParkingLots() throws RemoteException {
        return parkingLotRepository.findAll().size()==0?null:parkingLotRepository.findAll();
    }

    @Override
    public ParkingLot getParkingLotById(Long id) throws RemoteException {
        Optional <ParkingLot> optional = parkingLotRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        else{
            log.info("the parking not exist !!");
            return null;
        }
    }

    @Override
    public List<ParkingLot> getActiveParkingLots() throws RemoteException {
        return parkingLotRepository.findAllActive();
    }

    @Override
    public List<ParkingSpot> getAllSpotsByParkingLot(Long parkingLotId) throws RemoteException {
        Optional<ParkingLot> optional = parkingLotRepository.findById(parkingLotId);
        if(optional.isPresent()){
            return parkingSpotRepository.findByParkingLotId(parkingLotId);
        }
        else{
            log.info(" parking not exist !!! ");
            return null;
        }
    }

    @Override
    public List<ParkingSpot> getAvailableSpots(Long parkingLotId) throws RemoteException {
        Optional <ParkingLot> optional = parkingLotRepository.findById(parkingLotId);
        if (optional.isPresent()) {
            return parkingSpotRepository.findAccessibleSpotsByParkingLotId(parkingLotId);
        }
        else{
            log.info("parking not exist !!");
            return null;
        }
    }

    @Override
    public ParkingSpot getSpotById(Long spotId) throws RemoteException {
        Optional <ParkingSpot> optional = parkingSpotRepository.findById(spotId);
        if( optional.isPresent()){
            return optional.get();
        }
        else{
            log.info("parking spot not exist !!");
            return null;
        }
    }

    @Override
    public boolean updateSpotStatus(Long spotId, String status) throws RemoteException {
        Optional<ParkingSpot> optional = parkingSpotRepository.findById(spotId);
        if (!optional.isPresent()) {
            return false;
        }
        ParkingSpot parkingSpot = optional.get();
        parkingSpot.setStatus(SpotStatus.valueOf(status));
        parkingSpotRepository.save(parkingSpot);

        ParkingLot parkingLot = parkingSpot.getParkingLot();
        parkingLot.setAvailableSpots(getAvailableSpotsCount(parkingLot.getId()));
        parkingLotRepository.save(parkingLot);
        return true;
        
    }

    @Override
    public Reservation createReservation(Reservation reservationDTO) throws RemoteException {
        Optional <ParkingSpot> optional = parkingSpotRepository.findById(reservationDTO.getParkingSpot().getId());
        if(!optional.isPresent()||!optional.get().getStatus().equals(SpotStatus.AVAILABLE)){
            return null;
        }
        ParkingSpot parkingSpot = optional.get();
        ParkingLot parkingLot= parkingSpot.getParkingLot();

        reservationDTO.setParkingSpot(parkingSpot);
        reservationDTO.setParkingLot(parkingLot);

        parkingSpot.setCurrentReservation(reservationDTO);
        
        parkingSpotRepository.save(parkingSpot);

        Reservation currentReservation = reservationRepository.save(reservationDTO);

        parkingLot.setAvailableSpots(getAvailableSpotsCount(parkingLot.getId()));
        parkingLotRepository.save(parkingLot);

        return currentReservation;

    }

    @Override
    public Reservation getReservationById(Long id) throws RemoteException {

        Optional <Reservation> optional = reservationRepository.findById(id);
        if(!optional.isPresent()){
            log.info("Reservation not exist !!");
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Reservation> getReservationsByUserEmail(String email) throws RemoteException {
        return reservationRepository.findActiveReservationsByUserEmail(email);
    }

    @Override
    public boolean cancelReservation(Long id) throws RemoteException {
        Optional <Reservation> optional = reservationRepository.findById(id);
        if(!optional.isPresent()){
            return false;
        }
        Reservation reservation = optional.get();
        ParkingSpot parkingSpot = reservation.getParkingSpot();

        parkingSpot.setStatus(SpotStatus.AVAILABLE);
        parkingSpot.setCurrentReservation(null);
        parkingSpotRepository.save(parkingSpot);

        reservation.cancel("no reason");
        reservation.setParkingSpot(null);
        reservationRepository.save(reservation);

        ParkingLot parkingLot = parkingSpot.getParkingLot();
        parkingLot.setAvailableSpots(getAvailableSpotsCount(parkingLot.getId()));
        parkingLotRepository.save(parkingLot);

        return true;
    }

    @Override
    public int getTotalAvailableSpots() throws RemoteException {
        return parkingLotRepository.getTotalAvailableSpots();
    }

    @Override
    public double getOccupancyRate(Long parkingLotId) throws RemoteException {
        ParkingLot parking = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new RemoteException("Parking non trouvé"));
        
        int available = getAvailableSpotsCount(parkingLotId);
        int total = parking.getTotalSpots();
        
        if (total == 0) return 0.0;
        
        return ((double) (total - available) / total) * 100.0;
    }
    
}
