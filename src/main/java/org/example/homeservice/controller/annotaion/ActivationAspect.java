package org.example.homeservice.controller.annotaion;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActivationAspect {

    @Autowired
    private CustomerService customerRepository;

    @Before("@annotation(CheckActivation)")
    public void checkActivation(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();

            CustomerResponseDto customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));

            if (!customer.isActive()) {
                throw new DisabledException("Account not activated");
            }
        }
    }
}