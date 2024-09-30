package org.example.homeservice.service.admin;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.mapper.SpecialistMapper;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.entites.Admin;
import org.example.homeservice.entites.BaseUser;
import org.example.homeservice.entites.Specialist;
import org.example.homeservice.entites.enums.SpecialistStatus;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.example.homeservice.repository.service.ServiceRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl extends BaseEntityServiceImpl<Admin, Long, BaseEnitityRepo<Admin, Long>, SpecialistRequest, SpecialistResponse> implements AdminService {
    private final SpecialistRepo specialistRepo;
    private final ServiceRepo serviceRepo;
    private final SpecialistMapper specialistConverter; // Inject the converter

    @Autowired
    public AdminServiceImpl(BaseEnitityRepo<Admin, Long> baseRepo, SpecialistRepo specialistRepo, ServiceRepo serviceRepo, SpecialistMapper specialistConverter) {
        super(baseRepo);
        this.specialistRepo = specialistRepo;
        this.serviceRepo = serviceRepo;
        this.specialistConverter = specialistConverter; // Assign the injected converter
    }

    @Override
    public void saveSpecialist(SpecialistRequest specialistDTO) {
        if (specialistRepo.findByEmail(specialistDTO.email()).isPresent()) {
            throw new ValidationException("Specialist email already exists");
        }

        Specialist specialist = specialistConverter.toEntity(specialistDTO); // Use the converter
        specialistRepo.save(specialist);
    }

    @Override
    public void deleteSpecialistById(Long specialistId) {
        if (!specialistRepo.existsById(specialistId)) {
            throw new ValidationException("Specialist not found");
        }
        specialistRepo.deleteById(specialistId);
    }


    @Override
    public List<SpecialistResponse> getAllSpecialists() {
        List<Specialist> specialists = specialistRepo.findAll();
        if (specialists.isEmpty()) {
            throw new ValidationException("No specialists found");
        }
        return specialistConverter.toDto(specialists);
    }

    @Override
    public List<SpecialistResponse> getSpecialistsByStatus(SpecialistStatus status) {
        List<Specialist> specialists = specialistRepo.getSpecialistBySpecialistStatus(status);
        return specialistConverter.toDto(specialists);    }




    @Override
    public List<BaseUser> getAllUsers() {
        return List.of(); // Implement as necessary
    }

    @Override
    public void acceptSpecialist(Long specialistId) {
        Specialist specialist = specialistRepo.findById(specialistId)
                .orElseThrow(() -> new ValidationException("Specialist not found"));

        specialist.setSpecialistStatus(SpecialistStatus.APPROVED);
        specialistRepo.save(specialist);
    }

    @Override
    public void addingSpecialistToSubService(Long specialistId, Long subServiceId) {
        Specialist specialist = specialistRepo.findById(specialistId)
                .orElseThrow(() -> new ValidationException("Specialist not found"));

        // Verify specialist status if necessary

        org.example.homeservice.entites.Service foundService = serviceRepo.findById(subServiceId)
                .orElseThrow(() -> new ValidationException("Service not found"));

        // Logic to add specialist to the sub-service
        // Example: foundService.addSpecialist(specialist);
        // serviceRepo.save(foundService);
    }
}