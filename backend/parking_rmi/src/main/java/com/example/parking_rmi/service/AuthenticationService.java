package com.example.parking_rmi.service;


import com.example.parking_rmi.JWT.JwtService;
import com.example.parking_rmi.model.User.Role;
import com.example.parking_rmi.model.User;
import com.example.parking_rmi.Repo.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

   public Map<String, String> register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER); 
        repository.save(user);
        String jwtToken = jwtService.generateToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return response;
    }

    public Map<String, String> authenticate(User user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        
        User authenticatedUser = repository.findByEmail(user.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(authenticatedUser);
        
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return response;
    }
}