package com.example.parking_rmi.impliment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.Repository.ParkingLotRepository;
import com.example.parking_rmi.Repository.ParkingSpotRepository;
import com.example.parking_rmi.Repository.ReservationRepository;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.Reservation;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ParkingServiceImp extends UnicastRemoteObject implements ParkingService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationRepository reservationRepository;

    public ParkingServiceImp(ParkingLotRepository repo1, ParkingSpotRepository repo2, ReservationRepository repo3)
            throws RemoteException {
        super();
        this.parkingLotRepository = repo1;
        this.parkingSpotRepository = repo2;
        this.reservationRepository = repo3;
    }

    // ==================== PARKING LOTS ====================

    @Override
    public List<ParkingLotDTO> getAllParkingLots() throws RemoteException {
        List<ParkingLot> entities = parkingLotRepository.findAll();
        // Convert to DTOs, false = do not load lazy spots list
        return entities.stream().map(e -> mapToLotDTO(e, false)).collect(Collectors.toList());
    }

    @Override
    public ParkingLotDTO getParkingLotById(Long id) throws RemoteException {
        ParkingLot entity = parkingLotRepository.findByIdWithSpots(id).orElse(null);
        
        if (entity == null) return null;

        // Ensure the second parameter is true
        return mapToLotDTO(entity, true);
    }

    @Override
    public List<ParkingLotDTO> getActiveParkingLots() throws RemoteException {
        List<ParkingLot> entities = parkingLotRepository.findAllActive();
        return entities.stream().map(e -> mapToLotDTO(e, false)).collect(Collectors.toList());
    }

    // ==================== SPOTS ====================

    @Override
    public List<ParkingSpotDTO> getAllSpotsByParkingLot(Long parkingLotId) throws RemoteException {
        List<ParkingSpot> entities = parkingSpotRepository.findByParkingLotId(parkingLotId);
        return entities.stream().map(this::mapToSpotDTO).collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpotDTO> getAvailableSpots(Long parkingLotId) throws RemoteException {
        List<ParkingSpot> entities = parkingSpotRepository.findAccessibleSpotsByParkingLotId(parkingLotId);
        return entities.stream().map(this::mapToSpotDTO).collect(Collectors.toList());
    }

    @Override
    public ParkingSpotDTO getSpotById(Long spotId) throws RemoteException {
        return parkingSpotRepository.findById(spotId).map(this::mapToSpotDTO).orElse(null);
    }

    @Override
    public boolean updateSpotStatus(Long spotId, String status) throws RemoteException {
        ParkingSpot spot = parkingSpotRepository.findById(spotId).orElse(null);
        if (spot == null)
            return false;

        spot.setStatus(SpotStatus.valueOf(status));
        parkingSpotRepository.save(spot);

        // Update count in parent
        ParkingLot lot = spot.getParkingLot();
        lot.setAvailableSpots(parkingSpotRepository.countAvailableSpots(lot.getId()).intValue());
        parkingLotRepository.save(lot);
        return true;
    }

    // ==================== RESERVATIONS ====================

    @Override
    public ReservationDTO createReservation(ReservationDTO dto) throws RemoteException {
        ParkingSpot spot = parkingSpotRepository.findById(dto.getParkingSpotId()).orElse(null);

        if (spot == null || spot.getStatus() != SpotStatus.AVAILABLE)
            return null;

        Reservation entity = new Reservation();
        entity.setUserName(dto.getUserName());
        entity.setUserEmail(dto.getUserEmail());
        entity.setUserPhone(dto.getUserPhone());
        entity.setLicensePlate(dto.getLicensePlate());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setTotalAmount(dto.getTotalAmount());

        // Link relations
        entity.setParkingSpot(spot);
        entity.setParkingLot(spot.getParkingLot());

        // Save
        Reservation saved = reservationRepository.save(entity);

        // Update Spot
        spot.setStatus(SpotStatus.RESERVED);
        parkingSpotRepository.save(spot);

        return mapToResDTO(saved);
    }

    @Override
    public ReservationDTO getReservationById(Long id) throws RemoteException {
        return reservationRepository.findById(id).map(this::mapToResDTO).orElse(null);
    }

    @Override
    public List<ReservationDTO> getReservationsByUserEmail(String email) throws RemoteException {
        return reservationRepository.findActiveReservationsByUserEmail(email)
                .stream().map(this::mapToResDTO).collect(Collectors.toList());
    }

    @Override
    public boolean cancelReservation(Long id) throws RemoteException {
        Reservation res = reservationRepository.findById(id).orElse(null);
        if (res == null)
            return false;

        res.cancel("Cancelled by user");

        // Free spot
        ParkingSpot spot = res.getParkingSpot();
        if (spot != null) {
            spot.setStatus(SpotStatus.AVAILABLE);
            parkingSpotRepository.save(spot);
        }
        reservationRepository.save(res);
        return true;
    }

    // ==================== STATISTICS ====================

    @Override
    public int getAvailableSpotsCount(Long parkingLotId) throws RemoteException {
        Long count = parkingSpotRepository.countAvailableSpots(parkingLotId);
        return count != null ? count.intValue() : 0;
    }

    @Override
    public int getTotalAvailableSpots() throws RemoteException {
        Integer total = parkingLotRepository.getTotalAvailableSpots();
        return total != null ? total : 0;
    }

    @Override
    public double getOccupancyRate(Long parkingLotId) throws RemoteException {
        ParkingLot lot = parkingLotRepository.findById(parkingLotId).orElse(null);
        if (lot == null || lot.getTotalSpots() == 0)
            return 0.0;

        int available = getAvailableSpotsCount(parkingLotId);
        return ((double) (lot.getTotalSpots() - available) / lot.getTotalSpots()) * 100.0;
    }

    // ==================== CONVERSION HELPERS (Entity -> DTO) ====================

    private ParkingLotDTO mapToLotDTO(ParkingLot e, boolean deep) {
        ParkingLotDTO d = new ParkingLotDTO();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setAddress(e.getAddress());
        d.setCity(e.getCity());
        d.setLatitude(e.getLatitude());
        d.setLongitude(e.getLongitude());
        d.setTotalSpots(e.getTotalSpots());
        d.setAvailableSpots(e.getAvailableSpots());
        d.setRmiHost(e.getRmiHost());
        d.setRmiPort(e.getRmiPort());
        d.setRmiServiceName(e.getRmiServiceName());
        d.setStatus(e.getStatus().name());
        d.setHourlyRate(e.getHourlyRate());
        d.setOpeningTime(e.getOpeningTime());
        d.setClosingTime(e.getClosingTime());

        // ✅ SAFETY CHECK: Only map spots if 'deep' is true AND Hibernate actually
        // loaded them
        if (deep && e.getSpots() != null && Hibernate.isInitialized(e.getSpots())) {
            d.setParkingSpot(e.getSpots().stream().map(this::mapToSpotDTO).collect(Collectors.toList()));
        } else {
            d.setParkingSpot(new ArrayList<>()); // Return empty list if lazy or not requested
        }

        // ✅ SAFETY CHECK: Do same for reservations
        if (deep && e.getReservations() != null && Hibernate.isInitialized(e.getReservations())) {
            d.setReservations(e.getReservations().stream().map(this::mapToResDTO).collect(Collectors.toList()));
        } else {
            d.setReservations(new ArrayList<>());
        }

        return d;
    }

    private ParkingSpotDTO mapToSpotDTO(ParkingSpot e) {
        ParkingSpotDTO d = new ParkingSpotDTO();
        d.setId(e.getId());
        d.setSpotNumber(e.getSpotNumber());
        d.setStatus(e.getStatus().name());
        d.setSpotType(e.getSpotType().name());
        d.setFloorNumber(e.getFloorNumber());
        d.setSection(e.getSection());
        d.setIsAccessible(e.getIsAccessible());
        d.setParkingLotId(e.getParkingLot().getId()); // ID Only
        return d;
    }

    private ReservationDTO mapToResDTO(Reservation e) {
        ReservationDTO d = new ReservationDTO();
        d.setId(e.getId());
        d.setUserName(e.getUserName());
        d.setUserEmail(e.getUserEmail());
        d.setLicensePlate(e.getLicensePlate());
        d.setStartTime(e.getStartTime());
        d.setEndTime(e.getEndTime());
        d.setStatus(e.getStatus().name());
        if (e.getParkingLot() != null)
            d.setParkingLotId(e.getParkingLot().getId());
        if (e.getParkingSpot() != null)
            d.setParkingSpotId(e.getParkingSpot().getId());
        return d;
    }

    @Override
    public List<ReservationDTO> getReservationDTOsByParkingLot(long id) throws RemoteException {
        List<Reservation> reservations = reservationRepository.findByParkingLotId(id);
        List<ReservationDTO> reservationDTOs = new ArrayList<>(); 
        if(reservations !=null){
            for (Reservation reservation : reservations) {
                reservationDTOs.add(mapToResDTO(reservation));
            }
            return reservationDTOs;
        }
        else{
            return null;
        }
    }
}
