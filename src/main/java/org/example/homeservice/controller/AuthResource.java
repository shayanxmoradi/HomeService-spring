package org.example.homeservice.controller;

import lombok.Data;
import org.example.homeservice.controller.config.JwtTokenUtil;
import org.example.homeservice.controller.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;
import java.io.Serializable;

@RestController
public class AuthResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @Autowired
    private UserDetailsService userDetailsService;



//
//    @PostMapping("/authenticate")
//    public String createAuthenticationToken(@RequestParam String username, @RequestParam String password) throws Exception {
//        System.out.printf(username + " " + password);
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (AuthenticationException e) {
//            throw new Exception("Invalid credentials", e);
//        }
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        final String jwt = jwtTokenUtil.generateToken(userDetails);
//        //change here
//
//        return jwt;  // Return JWT to the client
//    }

    // Login endpoint to authenticate users and issue JWT
    @PostMapping("/login")
    public String  createAuthenticationToken(@RequestParam String username, @RequestParam String password) throws Exception {
        try {
            // Authenticate user with email and password
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password", e);
        }

        // If authentication is successful, generate JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().stream()
                .findFirst().get().getAuthority());

        // Return the JWT token in the response
        return jwt;
    }

    public record AuthenticationRequest(String email, String password) {}
}