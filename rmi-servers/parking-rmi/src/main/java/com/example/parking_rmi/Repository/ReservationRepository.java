package com.example.parking_rmi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.parking_rmi.model.Reservation;
import com.example.parking_rmi.model.Reservation.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des réservations
 * 
 * @author Omar - Backend Core
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

   List<Reservation> findByUserEmail(String userEmail);

    // ✅ OPTIMIZATION: Fetches Parking Lot & Spot details eagerly to avoid lazy errors in user history
    @Query("SELECT r FROM Reservation r " +
           "LEFT JOIN FETCH r.parkingLot " +
           "LEFT JOIN FETCH r.parkingSpot " +
           "WHERE r.userEmail = :email " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "ORDER BY r.startTime DESC")
    List<Reservation> findActiveReservationsByUserEmail(@Param("email") String email);

    List<Reservation> findByParkingLotId(Long parkingLotId);

    List<Reservation> findByParkingSpotId(Long parkingSpotId);

    @Query("SELECT r FROM Reservation r WHERE r.parkingSpot.id = :spotId " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND r.endTime > :now")
    Optional<Reservation> findActiveReservationBySpotId(@Param("spotId") Long spotId,
                                                        @Param("now") LocalDateTime now);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByParkingLotIdAndStatus(Long parkingLotId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.startTime BETWEEN :startDate AND :endDate " +
           "ORDER BY r.startTime")
    List<Reservation> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM Reservation r WHERE r.parkingSpot.id = :spotId " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((r.startTime BETWEEN :startTime AND :endTime) " +
           "OR (r.endTime BETWEEN :startTime AND :endTime) " +
           "OR (:startTime BETWEEN r.startTime AND r.endTime))")
    List<Reservation> findOverlappingReservations(@Param("spotId") Long spotId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMED' " +
           "AND r.endTime < :now")
    List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);

    Long countByStatus(ReservationStatus status);

    Long countByParkingLotId(Long parkingLotId);

    Long countByUserEmail(String userEmail);

    @Query("SELECT SUM(r.totalAmount) FROM Reservation r " +
           "WHERE r.parkingLot.id = :parkingLotId " +
           "AND r.status = 'COMPLETED'")
    Double getTotalRevenueByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    @Query("SELECT SUM(r.totalAmount) FROM Reservation r " +
           "WHERE r.status = 'COMPLETED' " +
           "AND r.startTime BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM Reservation r ORDER BY r.createdAt DESC")
    List<Reservation> findRecentReservations();

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Reservation r WHERE r.parkingSpot.id = :spotId " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND r.endTime > :now")
    boolean isSpotReserved(@Param("spotId") Long spotId, @Param("now") LocalDateTime now);
}