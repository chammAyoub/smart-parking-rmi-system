package com.example.parking_rmi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking_rmi.Interface.ParkingService;
import com.example.parking_rmi.dto.ParkingLotDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private ParkingService parkingService;

    @PostMapping("/addParking")
    public ResponseEntity<?> createParkingLResponseEnt(@RequestBody ParkingLotDTO entity)throws Exception {
        ParkingLotDTO parkingLotDTO =parkingService.createParkingLot(entity);
        if (parkingLotDTO!=null) {
            return ResponseEntity.ok("parking created");
        }
        return ResponseEntity.ok("error");
    }
    
}
