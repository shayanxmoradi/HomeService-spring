package org.example.homeservice.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.example.homeservice.domain.Customer;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
//public class ActivationFilter implements Filter {
//
//    @Autowired
//    private CustomerService customerRepository;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            String email = authentication.getName(); // Assuming email is used as the username
//
//            CustomerResponseDto customer = customerRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));
//
//            if (!customer.isActive()) {
//                throw new DisabledException("Account not activated");
//            }
//        }
//
//        chain.doFilter(request, response); // Continue with the request
//    }
//}