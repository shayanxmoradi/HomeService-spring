package org.example.homeservice.service.user;

import jakarta.persistence.EntityNotFoundException;
import org.example.homeservice.dto.SpecialistMapper;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.entites.Specialist;
import org.example.homeservice.repository.baseuser.SpecialistRepo;
import org.example.homeservice.repository.service.ServiceRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class SpeciallistServiceImpl extends BaseUserServiceImpl<Specialist, SpecialistRepo, SpecialistRequest,SpecialistResponse> implements SpeciallistService {
    private final ServiceRepo serviceRepo;
    private final SpecialistMapper specialistMapper;


    @Autowired
    public SpeciallistServiceImpl(@Qualifier("specialistRepo")SpecialistRepo baseRepo, ServiceRepo serviceRepo, SpecialistMapper specialistMapper) {
        super(baseRepo);
        this.serviceRepo = serviceRepo;
        this.specialistMapper = specialistMapper;

    }



    @Override
    public Optional<SpecialistResponse> findById(Long id) {
        return baseRepository.findById(id)
                .map(specialistMapper::toResponse);
    }

    @Override
    public List<SpecialistResponse> findAll() {
        return baseRepository.findAll().stream()
                .map(specialistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SpecialistResponse save(SpecialistRequest request) {
        Specialist specialist = specialistMapper.toEntity(request);
        // Additional logic for setting defaults, etc.
        Specialist savedSpecialist = baseRepository.save(specialist);
        return specialistMapper.toResponse(savedSpecialist);
    }

    @Override
    public SpecialistResponse update(Long id, SpecialistRequest request) {
        Specialist specialist = baseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found with id: " + id));

        // Update specialist fields
        specialist.setFirstName(request.firstName());
        specialist.setLastName(request.lastName());
        specialist.setEmail(request.email());
        specialist.setPassword(request.password()); // Ensure to hash the password
        specialist.setSpecialistStatus(request.specialistStatus());
        specialist.setRate(request.rate());
        specialist.setPersonalImage(request.personalImage());

        Specialist updatedSpecialist = baseRepository.save(specialist);
        return specialistMapper.toResponse(updatedSpecialist);
    }


    @Override
    public Optional<SpecialistRequest> login(String email, String password) {
        return Optional.empty();
    }
}