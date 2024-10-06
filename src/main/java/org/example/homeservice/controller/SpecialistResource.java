package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.Specialist;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.dto.UpdatePasswordRequst;
import org.example.homeservice.dto.UpdatePasswordResponse;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/speciallist")
@RequiredArgsConstructor
public class SpecialistResource {
    private final SpeciallistService speciallistService;

    @PostMapping
    public ResponseEntity<SpecialistResponse> createSpecialist(@Validated @RequestBody SpecialistRequest specialistRequest) {
        Optional<SpecialistResponse> specialist = speciallistService.save(specialistRequest);
        return specialist
                .map(specialistResponse -> ResponseEntity.status(HttpStatus.CREATED).body(specialistResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
    @PutMapping("/password")
    public ResponseEntity<UpdatePasswordResponse> updatePass(@RequestBody @Validated UpdatePasswordRequst updatePasswordRequst){
        speciallistService.updatePassword(updatePasswordRequst);
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse(updatePasswordRequst.email(), "password suffessfully changed for this user.");
        return ResponseEntity.ok(updatePasswordResponse);    }

}
