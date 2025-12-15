package com.example.parking_rmi.service;

import com.example.parking_rmi.Repository.ParkingLotRepository;
import com.example.parking_rmi.Repository.ParkingSpotRepository;
import com.example.parking_rmi.dto.ParkingSpotDTO;
import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Transactional(readOnly = true)
    public List<ParkingSpotDTO> findByStatus(SpotStatus status) {
        return parkingSpotRepository.findByStatus(status).stream()
                .map(this::mapToSpotDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParkingSpotDTO> findByParkingLotIdAndStatus(Long parkingLotId, SpotStatus status) {
        return parkingSpotRepository.findByParkingLotIdAndStatus(parkingLotId, status).stream()
            .map(this::mapToSpotDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long countAvailableSpots(Long parkingLotId) {
        return parkingSpotRepository.countAvailableSpots(parkingLotId);
    }

    @Transactional(readOnly = true)
    public Long countOccupiedSpots(Long parkingLotId) {
        return parkingSpotRepository.countOccupiedSpots(parkingLotId);
    }
    

    @Transactional(readOnly = true)
    public List<ParkingSpotDTO> getAllSpotsByParkingLot(Long parkingLotId) {
        return parkingSpotRepository.findByParkingLotId(parkingLotId).stream()
                .map(this::mapToSpotDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParkingSpotDTO> getAvailableSpots(Long parkingLotId) {
        return parkingSpotRepository.findAccessibleSpotsByParkingLotId(parkingLotId).stream()
                .map(this::mapToSpotDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParkingSpotDTO getSpotById(Long spotId) {
        return parkingSpotRepository.findById(spotId)
                .map(this::mapToSpotDTO).orElse(null);
    }

    // 🚨 TRANSACTIONAL: Updates Spot AND ParkingLot
    @Transactional
    public boolean updateSpotStatus(Long spotId, String statusStr) {
        ParkingSpot spot = parkingSpotRepository.findById(spotId).orElse(null);
        if (spot == null)
            return false;

        try {
            SpotStatus newStatus = SpotStatus.valueOf(statusStr);
            spot.setStatus(newStatus);
            parkingSpotRepository.save(spot);

            // Update parent count
            ParkingLot lot = spot.getParkingLot();
            if (lot != null) {
                // Assuming you have a count query in repo
                Long count = parkingSpotRepository.countAvailableSpots(lot.getId());
                lot.setAvailableSpots(count != null ? count.intValue() : 0);
                parkingLotRepository.save(lot);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(readOnly = true)
    public int getAvailableSpotsCount(Long parkingLotId) {
        Long count = parkingSpotRepository.countAvailableSpots(parkingLotId);
        return count != null ? count.intValue() : 0;
    }

    @Transactional(readOnly = true)
    public double getOccupancyRate(Long parkingLotId) {
        ParkingLot lot = parkingLotRepository.findById(parkingLotId).orElse(null);
        if (lot == null || lot.getTotalSpots() == 0)
            return 0.0;

        int available = getAvailableSpotsCount(parkingLotId);
        return ((double) (lot.getTotalSpots() - available) / lot.getTotalSpots()) * 100.0;
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
        if (e.getParkingLot() != null) {
            d.setParkingLotId(e.getParkingLot().getId());
        }
        return d;
    }

    @Transactional
    public boolean simulateCarEntry(Long spotId) {
        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        if (spot.getStatus() == SpotStatus.OCCUPIED) {
            return false;
        }


        if (spot.getStatus() == SpotStatus.AVAILABLE) {
            ParkingLot lot = spot.getParkingLot();
            if (lot.getAvailableSpots() > 0) {
                lot.setAvailableSpots(lot.getAvailableSpots() - 1);
                parkingLotRepository.save(lot);
            }
        }

        spot.setStatus(SpotStatus.OCCUPIED);
        spot.setLastOccupiedAt(LocalDateTime.now()); 
        parkingSpotRepository.save(spot);
        return true;
    }

    // 🚪 CAR EXIT SIMULATION
    @Transactional
    public boolean simulateCarExit(Long spotId) {
        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        if (spot.getStatus() == SpotStatus.AVAILABLE) {
            return false;
        }

        // Free up the spot
        spot.setStatus(SpotStatus.AVAILABLE);
        parkingSpotRepository.save(spot);

        ParkingLot lot = spot.getParkingLot();
        if (lot.getAvailableSpots() < lot.getTotalSpots()) {
            lot.setAvailableSpots(lot.getAvailableSpots() + 1);
            parkingLotRepository.save(lot);
        }
        return true;
    }
}