package com.example.parking_rmi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.ParkingLotDTO;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;

import lombok.var;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping("/addParking")
    public ResponseEntity<?> createParkingLResponseEnt(@RequestBody ParkingLotDTO entity) throws Exception {
        ParkingLotDTO parkingLotDTO = parkingService.createParkingLot(entity);
        if (parkingLotDTO != null) {
            return ResponseEntity.ok("parking created");
        }
        return ResponseEntity.ok("error");
    }

    @PostMapping("/simulate/enter/{spotId}")
    public ResponseEntity<?> simulateEntry(@PathVariable long spotId) throws Exception {
        var test = parkingService.simulateCarEntry(spotId);
        if (test) {
            return ResponseEntity.ok("car enter");
        }
        return ResponseEntity.ok("error");
    }

    @PostMapping("/simulate/exit/{spotId}")
    public ResponseEntity<?> simulateExite(@PathVariable long spotId) throws Exception {
        var test = parkingService.simulateCarExit(spotId);
        if (test) {
            return ResponseEntity.ok("car exit");
        }
        return ResponseEntity.ok("error");
    }

    @GetMapping("/stats/{status}")
    public ResponseEntity<?> getSpotsByStatus(@PathVariable SpotStatus status)throws Exception{
        System.out.println(status);
        var list = parkingService.findByStatus(status);
        if(list ==null){
            return ResponseEntity.badRequest().body("errpr");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/stats/{parkingId}/{status}")
    public ResponseEntity<?> getSpotsByParkingAndStatus(@PathVariable long parkingId,@PathVariable SpotStatus status)throws Exception{
        var list = parkingService.findByParkingLotIdAndStatus(parkingId, status);
        if(list==null){
            return ResponseEntity.badRequest().body("error");
        }
        return ResponseEntity.ok(list);

    }

    @GetMapping("/stats/parking/{parkingId}")
    public ResponseEntity<?> getStatofParking(@PathVariable long parkingId)throws Exception{
        Map<String, Object> stats = new HashMap<>();

        Long available = parkingService.countAvailableSpots(parkingId);
        var list = parkingService.getAllSpotsByParkingLot(parkingId);
        System.out.println(list.size());
        Long occupied = list.size()-available;
        
        Double revenue = parkingService.getTotalRevenueByParkingLotId(parkingId);

        // 3. Build the response map
        stats.put("parkingLotId", parkingId);
        stats.put("availableSpots", available);
        stats.put("occupiedSpots", occupied);
        stats.put("totalRevenue", revenue);
        double total =available+occupied;
        // Calculate Occupancy Rate manually here (since we have the raw numbers)
        double occupancyRate = (total == 0) ? 0.0 : ((double) occupied / total) * 100;
        stats.put("occupancyRate", Math.round(occupancyRate * 100.0) / 100.0); // Round to 2 decimals
        stats.put("occupancyRate", occupancyRate);
        System.out.println(stats);

        return ResponseEntity.ok(stats);
    }
}
