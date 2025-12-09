package com.example.parking_rmi.controller;

import com.example.parking_rmi.Repo.UserRepository;
import com.example.parking_rmi.model.User;
import com.example.parking_rmi.service.AuthenticationService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthenticationController {
    @Autowired
    private AuthenticationService service;

    @Autowired
    private UserRepository repository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody User user) {
        Map<String,Object> resp = new HashMap<>();

        resp.put("token",service.authenticate(user).get("token"));
        resp.put("user", repository.findByEmail(user.getEmail()));
        return ResponseEntity.ok(resp);
    }
}