package com.example.parking_rmi.service;

import com.example.parking_rmi.Repository.ParkingSpotRepository;
import com.example.parking_rmi.Repository.ReservationRepository;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;
import com.example.parking_rmi.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationRepository reservationRepository;

    // ✅ Put @Transactional HERE. This is safe.
    @Transactional
    public ReservationDTO createReservation(ReservationDTO dto) {
        // 1. Fetch Spot
        ParkingSpot spot = parkingSpotRepository.findById(dto.getParkingSpotId()).orElse(null);
        if (spot == null || spot.getStatus() != SpotStatus.AVAILABLE) {
            throw new IllegalArgumentException("Spot is null or not available");
        }

        // 2. Create Entity & Map Basic Fields
        Reservation entity = new Reservation();
        entity.setUserName(dto.getUserName());
        entity.setUserEmail(dto.getUserEmail());
        entity.setUserPhone(dto.getUserPhone());
        entity.setLicensePlate(dto.getLicensePlate());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // 3. 🛡️ SMART DATE LOGIC (Fixes the NullPointer)
        entity.setStartTime(dto.getStartTime());

        // Case A: User sent Start + End
        if (dto.getEndTime() != null) {
            entity.setEndTime(dto.getEndTime());
            long calculatedHours = java.time.Duration.between(entity.getStartTime(), entity.getEndTime()).toHours();
            entity.setDurationHours(Math.max(1, (int) calculatedHours));
        }
        // Case B: User sent Start + Duration (Calculate End)
        else if (dto.getDurationHours() != null) {
            entity.setDurationHours(dto.getDurationHours());
            entity.setEndTime(entity.getStartTime().plusHours(dto.getDurationHours()));
        }
        // Case C: Missing Data
        else {
            throw new IllegalArgumentException("You must provide either (Start + End) or (Start + Duration)");
        }

        // 4. Calculate Price
        // Now it is safe to calculate because we guaranteed Duration and Dates are set
        BigDecimal hourlyRate = spot.getParkingLot().getHourlyRate();
        entity.setTotalAmount(hourlyRate.multiply(new BigDecimal(entity.getDurationHours())));

        // 5. Link Relations
        entity.setParkingSpot(spot);
        entity.setParkingLot(spot.getParkingLot());

        // 6. Save & Update Spot
        Reservation saved = reservationRepository.save(entity);

        spot.setStatus(SpotStatus.RESERVED);
        parkingSpotRepository.save(spot);

        return mapToResDTO(saved);
    }

    // Helper method to map Entity -> DTO
    private ReservationDTO mapToResDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return ReservationDTO.builder()
                .id(reservation.getId())

                // User Information
                .userName(reservation.getUserName())
                .userEmail(reservation.getUserEmail())
                .userPhone(reservation.getUserPhone())
                .licensePlate(reservation.getLicensePlate())

                // Reservation Details
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .durationHours(reservation.getDurationHours())

                // Status & Payment
                // Note: If your DTO uses Strings for enums, use .name()
                .status(reservation.getStatus().toString())
                .totalAmount(reservation.getTotalAmount())
                .paymentStatus(reservation.getPaymentStatus().toString())

                // Lifecycle Timestamps
                .checkInTime(reservation.getCheckInTime())
                .checkOutTime(reservation.getCheckOutTime())
                .cancellationReason(reservation.getCancellationReason())

                // Relations (Extract IDs safely)
                .parkingLotId(reservation.getParkingLot() != null ? reservation.getParkingLot().getId() : null)
                .parkingSpotId(reservation.getParkingSpot() != null ? reservation.getParkingSpot().getId() : null)

                .build();
    }

    @Transactional(readOnly = true)
    public ReservationDTO getReservationById(Long id) {
        return reservationRepository.findById(id).map(this::mapToResDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationsByUserEmail(String email) {
        return reservationRepository.findActiveReservationsByUserEmail(email).stream()
                .map(this::mapToResDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationDTOsByParkingLot(long lotId) {
        return reservationRepository.findByParkingLotId(lotId).stream()
                .map(this::mapToResDTO).collect(Collectors.toList());
    }

    // 🚨 TRANSACTIONAL: Updates Reservation AND frees the Spot
    @Transactional
    public boolean cancelReservation(Long id) {
        Reservation res = reservationRepository.findById(id).orElse(null);
        if (res == null)
            return false;

        res.cancel("Cancelled by user"); // Updates Reservation status

        // Free the spot
        ParkingSpot spot = res.getParkingSpot();
        if (spot != null) {
            spot.setStatus(SpotStatus.AVAILABLE);
            parkingSpotRepository.save(spot);
        }
        reservationRepository.save(res);
        return true;
    }

    // Helper

}