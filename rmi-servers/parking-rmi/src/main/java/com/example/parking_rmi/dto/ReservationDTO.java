package com.example.parking_rmi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String licensePlate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationHours;
    private String status;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String cancellationReason;
    
    private Long parkingLotId;  // ID only
    private Long parkingSpotId; // ID only
}