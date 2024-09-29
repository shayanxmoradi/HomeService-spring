package org.example.homeservice.service.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.example.homeservice.dto.*;
import org.example.homeservice.entites.Customer;
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
                .map(specialistMapper::toDto);
    }

//    @Override
//    public List<SpecialistResponse> findAll() {
//        return baseRepository.findAll().stream()
//                .map(specialistMapper::toDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public Optional<SpecialistResponse> save(SpecialistRequest request) {
        if (baseRepository.findByEmail(request.email()).isPresent()) {
            throw new ValidationException("Customer with this email already exists");
        }

        // Convert DTO to entity and save
        Specialist customer = SpecialistMapper.INSTANCE.toEntity(request);
        Specialist savedSpelist = baseRepository.save(customer);
        return Optional.of(SpecialistMapper.INSTANCE.toDto(savedSpelist));
    }





    @Override
    public Optional<SpecialistResponse> login(String email, String password) {
        return Optional.ofNullable(toDto(baseRepository.findByEmailAndPassword(email, password).get()));
    }

}