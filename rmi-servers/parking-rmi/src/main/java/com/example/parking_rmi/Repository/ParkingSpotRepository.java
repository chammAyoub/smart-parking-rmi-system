package com.example.parking_rmi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;
import com.example.parking_rmi.model.ParkingSpot.SpotType;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des places de parking
 * 
 * @author Omar - Backend Core
 */
@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findByParkingLotId(Long parkingLotId);

    Optional<ParkingSpot> findByParkingLotIdAndSpotNumber(Long parkingLotId, String spotNumber);

    @Query("SELECT s FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'AVAILABLE'")
    List<ParkingSpot> findAvailableSpotsByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    List<ParkingSpot> findByStatus(SpotStatus status);//1

    List<ParkingSpot> findByParkingLotIdAndStatus(Long parkingLotId, SpotStatus status);//2

    List<ParkingSpot> findBySpotType(SpotType spotType);

    List<ParkingSpot> findByParkingLotIdAndSpotType(Long parkingLotId, SpotType spotType);

    @Query("SELECT s FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.isAccessible = true")
    List<ParkingSpot> findAccessibleSpotsByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    @Query("SELECT s FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.isElectricCharging = true")
    List<ParkingSpot> findElectricSpotsByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    Long countByParkingLotIdAndStatus(Long parkingLotId, SpotStatus status);

    // ✅ This is the method used by your Service implementation
    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'AVAILABLE'")
    Long countAvailableSpots(@Param("parkingLotId") Long parkingLotId);//3

    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'OCCUPIED'")
    Long countOccupiedSpots(@Param("parkingLotId") Long parkingLotId);//4

    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'RESERVED'")
    Long countReservedSpots(@Param("parkingLotId") Long parkingLotId);

    boolean existsByParkingLotIdAndSpotNumber(Long parkingLotId, String spotNumber);

    List<ParkingSpot> findByParkingLotIdAndFloorNumber(Long parkingLotId, Integer floorNumber);

    List<ParkingSpot> findByParkingLotIdAndSection(Long parkingLotId, String section);
}