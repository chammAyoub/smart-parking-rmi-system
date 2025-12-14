package com.example.parking_rmi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.ParkingLotDTO;

import lombok.var;

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

}
