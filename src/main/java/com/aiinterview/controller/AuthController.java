//package com.aiinterview.controller;
//
//import com.aiinterview.model.User;
//import com.aiinterview.service.JwtService;
//import com.aiinterview.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashSet;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final UserService userService;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    @CrossOrigin(origins = "http://localhost:5174")
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody User user) {
//        if (userService.existsByEmail(user.getEmail())) {
//            return ResponseEntity.badRequest().body("Email already registered");
//        }
//
//        if (user.getRoles() == null) {
//            user.setRoles(new HashSet<>());
//        }
//        user.getRoles().add("USER");
//        User savedUser = userService.registerUser(user);
//        String token = jwtService.generateToken(savedUser.getEmail());
//
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
//
//    @CrossOrigin(origins = "http://localhost:5174")
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//            );
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(401).body("Invalid email or password");
//        }
//
//        String token = jwtService.generateToken(request.getEmail());
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
//}
//
package com.aiinterview.controller;

import com.aiinterview.model.User;
import com.aiinterview.service.JwtService;
import com.aiinterview.service.UserDetailsServiceImpl;
import com.aiinterview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add("USER");

        User savedUser = userService.registerUser(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
