package org.example.homeservice.controller;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.homeservice.controller.annotaion.CheckActivation;
import org.example.homeservice.controller.annotaion.CheckSpecActivation;
import org.example.homeservice.domain.Wallet;
import org.example.homeservice.dto.customer.CustomerRequsetDto;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.dto.updatepassword.UpdatePasswordRequst;
import org.example.homeservice.dto.updatepassword.UpdatePasswordResponse;
import org.example.homeservice.service.WalletService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerResource {
    private final CustomerService customerService;

    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return customerService.findAll()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Validated CustomerRequsetDto customer) {

        Optional<CustomerResponseDto> savedCustomer = customerService.addCustomer(customer);
        return savedCustomer
                .map(cust -> ResponseEntity.status(HttpStatus.CREATED).body(cust))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/password")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestBody @Validated UpdatePasswordRequst updatePasswordRequst) {
        customerService.updatePassword(updatePasswordRequst);
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse(updatePasswordRequst.email(), "password suffessfully changed for this user.");
        return ResponseEntity.ok(updatePasswordResponse);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<List<CustomerResponseDto>> filterCustomers(@RequestParam(required = false) Long numberOfOrders,
                                                                     @RequestParam(required = false) LocalDateTime startDate,
                                                                     @RequestParam(required = false) LocalDateTime endDate,
                                                                     @RequestParam(required = false) String firstName,
                                                                     @RequestParam(required = false) String lastName,
                                                                     @Email @Validated @RequestParam(required = false) String email) {
        return ResponseEntity.ok(customerService.filterCustomers(firstName, lastName, email, startDate, endDate, numberOfOrders));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN')")

    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        Optional<CustomerResponseDto> customer = customerService.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN')")
    @CheckActivation

    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable Long id,
                                                              @RequestBody @Validated CustomerRequsetDto customerRequestDto) {
        Optional<CustomerResponseDto> updatedCustomer = customerService.update(customerRequestDto.withId(id));
        return updatedCustomer
                .map(cust -> ResponseEntity.ok(cust))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (customerService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/wallet")
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('SPECIALIST')")
    @CheckActivation
//    @CheckSpecActivation
    public ResponseEntity<Double> getAllCustomersWithWallet(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();

        Optional<CustomerResponseDto> optionalCustomer = customerService.findByEmail(userEmail);
        if (optionalCustomer.isPresent()) {
            Long walletId = optionalCustomer.get().walletId();

            try {
                Wallet optionalWallet = walletService.findById(walletId);

                double creditAmount = optionalWallet.getCreditAmount();
                return ResponseEntity.ok(creditAmount);

            } catch (ValidationException e) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }


        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
