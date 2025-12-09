package com.example.parking_rmi.service;


import com.example.parking_rmi.Repository.ParkingLotRepository;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.model.ParkingLot;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    // We might need to inject other services if we need their DTO mappers,
    // but for now, we will keep simple mappers here to avoid circular dependencies.

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