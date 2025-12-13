package com.example.parking_rmi.service;

import com.example.parking_rmi.Repository.ParkingLotRepository;
import com.example.parking_rmi.Repository.ParkingSpotRepository;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingLot.ParkingStatus;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;
import com.example.parking_rmi.model.ParkingSpot.SpotType;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    // We might need to inject other services if we need their DTO mappers,
    // but for now, we will keep simple mappers here to avoid circular dependencies.
    private final ParkingSpotRepository parkingSpotRepository; // <--- INJECT THIS

    // ==========================================
    // 🆕 CREATE METHOD (Auto-Generates Spots)
    // ==========================================
    @Transactional
    public ParkingLotDTO createParkingLot(ParkingLotDTO dto) {
        // 1. Convert DTO to Entity
        ParkingLot lot = new ParkingLot();
        lot.setName(dto.getName());
        lot.setAddress(dto.getAddress());
        lot.setCity(dto.getCity());
        lot.setLatitude(dto.getLatitude());
        lot.setLongitude(dto.getLongitude());
        lot.setTotalSpots(dto.getTotalSpots());
        lot.setAvailableSpots(dto.getTotalSpots()); // Initially, all are available
        lot.setHourlyRate(dto.getHourlyRate());
        lot.setOpeningTime(dto.getOpeningTime());
        lot.setClosingTime(dto.getClosingTime());
       

        // Default RMI / Status settings
        lot.setRmiHost(dto.getRmiHost() != null ? dto.getRmiHost() : "localhost");
        lot.setRmiPort(dto.getRmiPort() != null ? dto.getRmiPort() : 1099);
        lot.setRmiServiceName(dto.getRmiServiceName());
        lot.setStatus(ParkingStatus.ACTIVE);
        lot.setCreatedAt(LocalDateTime.now());
        lot.setUpdatedAt(LocalDateTime.now());
        System.out.println(lot);
        
        // 2. Save the Lot first (to generate the ID)
        ParkingLot savedLot = parkingLotRepository.save(lot);

        // 3. 🪄 AUTOMATICALLY GENERATE SPOTS
        if (savedLot.getTotalSpots() > 0) {
            List<ParkingSpot> generatedSpots = new ArrayList<>();

            for (int i = 1; i <= savedLot.getTotalSpots(); i++) {
                ParkingSpot spot = new ParkingSpot();
                // Naming convention: "A-1", "A-2", etc.
                spot.setSpotNumber("A-" + i);
                spot.setFloorNumber(1);
                spot.setSection("A");
                spot.setStatus(SpotStatus.AVAILABLE); // Default status
                spot.setSpotType(SpotType.STANDARD);

                // Defaults for features
                spot.setIsAccessible(false);
                spot.setIsCovered(true);
                spot.setIsElectricCharging(false);

                // Link to Parent
                spot.setParkingLot(savedLot);

                generatedSpots.add(spot);
            }
            // Batch save for performance
            parkingSpotRepository.saveAll(generatedSpots);
        }

        // 4. Return DTO
        return mapToLotDTO(savedLot, false);
    }

    @Transactional(readOnly = true)
    public List<ParkingLotDTO> getAllParkingLots() {
        return parkingLotRepository.findAll().stream()
                .map(e -> mapToLotDTO(e, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParkingLotDTO getParkingLotById(Long id) {
        // Use findByIdWithSpots if you have that custom query, otherwise findById
        // The @Transactional annotation keeps the session open for Lazy Loading
        ParkingLot entity = parkingLotRepository.findById(id).orElse(null);
        return (entity != null) ? mapToLotDTO(entity, true) : null;
    }

    @Transactional(readOnly = true)
    public List<ParkingLotDTO> getActiveParkingLots() {
        return parkingLotRepository.findAllActive().stream()
                .map(e -> mapToLotDTO(e, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public int getTotalAvailableSpots() {
        Integer total = parkingLotRepository.getTotalAvailableSpots();
        return total != null ? total : 0;
    }

    // --- Helper Mapper ---
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

        if (deep && e.getSpots() != null && Hibernate.isInitialized(e.getSpots())) {
            // Simplified spot mapping to avoid circular dependency
            d.setParkingSpot(new ArrayList<>());
            // In a real app, use a dedicated Mapper class
        }
        return d;
    }
}