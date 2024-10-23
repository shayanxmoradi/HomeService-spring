package org.example.homeservice.controller;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.controller.config.JwtUtil;
import org.example.homeservice.domain.BaseUser;
import org.example.homeservice.domain.Customer;
import org.example.homeservice.domain.Specialist;
import org.example.homeservice.domain.VerificationToken;
import org.example.homeservice.dto.customer.CustomerMapper;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.service.auth.EmailService;
import org.example.homeservice.service.auth.VerficationService;
import org.example.homeservice.service.auth.VerificationServiceImpl;
import org.example.homeservice.service.user.customer.CustomerService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AuthResource {
    private final AuthenticationManager authenticationManager;


    private final JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    private final EmailService emailService;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final SpeciallistService speciallistService;
    private final SpecialistMapper specialistMapper;

    private final VerficationService verificationService;

    @GetMapping("/activate")
    public String activateAccount(@RequestParam("token") String token) {
        VerificationToken verificationToken = verificationService.findByToken(token);

        if (verificationToken == null
//                || verificationToken.isExpired()

        ) {
            return "error";
        }
        if (verificationToken.getIsUsed()) {
            return "token alread used";
        }
        Long userId = verificationToken.getUser().getId();

try {

    customerService.activateCustomer(userId);
}catch (Exception e) {

    speciallistService.activateSpecialist(userId);
}


            verificationToken.setIsUsed(true);
            verificationService.save(verificationToken);


        return "activated";
    }

    @PostMapping("/customer/register/{id}")
    public String registerUser(@PathVariable Long id) {
        System.out.println(id);
        Customer customer = customerMapper.toEntity(customerService.findById(id).get());
if (customer.getIsActive())return "this account is already active";
        VerificationToken token = new VerificationToken();
        token.setUser(customer);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationService.save(token);

        emailService.sendActivationEmail(customer.getEmail(), token.getToken());

        return token.getToken();
    }

    @PostMapping("specialist/register/{id}")
    public String registerSpecialist(@PathVariable Long id) {
        System.out.println(id);
        Specialist customer = specialistMapper.toEntity(speciallistService.findById(id).get());
        System.out.println(customer.getIsActive());
        if (customer.getIsActive())return "this account is already active";

        VerificationToken token = new VerificationToken();
        token.setUser(customer);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationService.save(token);

        emailService.sendActivationEmail(customer.getEmail(), token.getToken());

        return token.getToken();
    }




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

