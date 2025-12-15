package com.example.parking_rmi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingLot.ParkingStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des parkings
 * 
 * @author Omar - Backend Core
 */
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    
   // ✅ CRITICAL FIX: Eagerly load spots to prevent LazyInitializationException in RMI
    @Query("SELECT p FROM ParkingLot p LEFT JOIN FETCH p.spots WHERE p.id = :id")
    Optional<ParkingLot> findByIdWithSpots(@Param("id") Long id);

    Optional<ParkingLot> findByName(String name);

    Optional<ParkingLot> findByRmiServiceName(String rmiServiceName);

    List<ParkingLot> findByStatus(ParkingStatus status);//1

    @Query("SELECT p FROM ParkingLot p WHERE p.status = 'ACTIVE'")
    List<ParkingLot> findAllActive();

    @Query("SELECT p FROM ParkingLot p WHERE p.status = 'ACTIVE' AND p.availableSpots > 0")
    List<ParkingLot> findAllWithAvailableSpots();

    List<ParkingLot> findByCity(String city);

    // Geo-location query
    @Query("SELECT p FROM ParkingLot p WHERE p.status = 'ACTIVE' " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
           "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(p.latitude)))) < :radius")
    List<ParkingLot> findNearby(@Param("latitude") Double latitude,
                                @Param("longitude") Double longitude,
                                @Param("radius") Double radius);

    Long countByStatus(ParkingStatus status);

    @Query("SELECT SUM(p.availableSpots) FROM ParkingLot p WHERE p.status = 'ACTIVE'")
    Integer getTotalAvailableSpots();

    @Query("SELECT SUM(p.totalSpots) FROM ParkingLot p WHERE p.status = 'ACTIVE'")
    Integer getTotalSpots();

    boolean existsByName(String name);

    boolean existsByRmiServiceName(String rmiServiceName);
}