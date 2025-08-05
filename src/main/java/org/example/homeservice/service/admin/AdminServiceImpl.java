package org.example.homeservice.service.admin;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.example.homeservice.dto.service.ServiceRequest;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.service.ServiceMapper;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.dto.specialist.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.domain.user.BaseUser;
import org.example.homeservice.domain.user.Specialist;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.repository.user.SpecialistRepo;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final SpecialistRepo specialistRepo;
    private final SpecialistMapper specialistConverter; // Inject the converter
    private final ServiceService serviceService;
    private final SpeciallistService speciallistService;
    private final ServiceMapper serviceMapper;

    @Autowired
    public AdminServiceImpl(SpecialistRepo specialistRepo, SpecialistMapper specialistConverter, ServiceService serviceService, SpeciallistService speciallistService, ServiceMapper serviceMapper) {

        this.specialistRepo = specialistRepo;
        this.specialistConverter = specialistConverter;
        this.serviceService = serviceService;
        this.speciallistService = speciallistService;
        this.serviceMapper = serviceMapper;
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
        return specialistConverter.toDto(specialists);
    }


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

    /**
     * when you find object its still in persitst state dont need to call update on it again
     *
     * @param specialistId
     * @param subServiceId
     */
    @Transactional
    @Override
    public void addingSpecialistToSubService(Long specialistId, Long subServiceId) {


        Specialist specialist = specialistRepo.findById(specialistId)
                .orElseThrow(() -> new ValidationException("Specialist not found"));

        if (specialist.getSpecialistStatus() != SpecialistStatus.APPROVED) {
            throw new ValidationException("Specialist is not approved");
        }
        System.out.println("is avil" + serviceService.isSpecialistAvailableInService(subServiceId, specialistId));
        if (serviceService.isSpecialistAvailableInService(subServiceId, specialistId)) {
            throw new ValidationException("Specialist is already added to this service");
        }
        org.example.homeservice.domain.service.Service service = serviceService.findByIdX(subServiceId);

        ServiceResponse foundService = serviceService.findById(subServiceId)
                .orElseThrow(() -> new ValidationException("Service not found"));
        service.getAvilableSpecialists().add(specialist);

    }

    @Transactional
    @Override
    public void deleteSpecialistFromSubService(Long specialistId, Long subServiceId) {
        Specialist specialist = specialistRepo.findById(specialistId)
                .orElseThrow(() -> new ValidationException("Specialist with : " + specialistId + " found"));

        System.out.println("is avil" + serviceService.isSpecialistAvailableInService(subServiceId, specialistId));
        if (!serviceService.isSpecialistAvailableInService(subServiceId, specialistId)) {
            throw new ValidationException("Specialist is already not in this service ");
        }
        org.example.homeservice.domain.service.Service service = serviceService.findByIdX(subServiceId);

        ServiceResponse foundService = serviceService.findById(subServiceId)
                .orElseThrow(() -> new ValidationException("Service not found"));
        service.getAvilableSpecialists().remove(specialist);

    }

    @Override
    public Optional<ServiceResponse> createNewService(ServiceRequest dto) {
        return serviceService.save(dto);
    }

    @Override
    public Optional<SpecialistResponse> addSpeciliast(SpecialistRequest request) {
        return speciallistService.save(request);
    }

    @Override
    public void deleteSpecialist(Long specialistId) {
        specialistRepo.deleteById(specialistId);
    }

    @Override
    public Optional<List<ServiceResponse>> findAllServices() {
        return serviceService.findAll();
    }


}