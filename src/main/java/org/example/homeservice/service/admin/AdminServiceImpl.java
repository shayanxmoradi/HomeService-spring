package org.example.homeservice.service.admin;

import jakarta.validation.ValidationException;
import org.example.homeservice.entites.Admin;
import org.example.homeservice.entites.BaseUser;
import org.example.homeservice.entites.Specialist;
import org.example.homeservice.entites.enums.SpecialistStatus;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.example.homeservice.repository.baseuser.SpecialistRepo;
import org.example.homeservice.repository.service.ServiceRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl<D,RDTO> extends BaseEntityServiceImpl<Admin, Long, BaseEnitityRepo<Admin,Long>,D,RDTO> implements AdminService {
    private final SpecialistRepo specialistRepo;
    private final ServiceRepo serviceRepo;

    @Autowired
    public AdminServiceImpl(BaseEnitityRepo<Admin,Long> baseRepo, SpecialistRepo specialistRepo, ServiceRepo serviceRepo) {
        super(baseRepo);
        this.specialistRepo = specialistRepo;
        this.serviceRepo = serviceRepo;
    }
    public void saveSpecialist(SavingSpecialistDTO specialistDTO) {
        if (specialistRepo.findByEmail( specialistDTO.getEmail()).isPresent()) {
            throw new ValidationException("Specialist email already exists");
        }

        Specialist specialist = convertToEntity(specialistDTO);
        specialistRepo.save(specialist);
    }

    @Override
    public void deleteSpcialistById(Long specialistId) {
        if (!specialistRepo.existsById(specialistId)) {
            throw new ValidationException("Specialist not found");
        }
        specialistRepo.deleteById(specialistId);

    }

    @Override
    public List<Specialist> getAllSpecialist() {
        List<Specialist> specialists = specialistRepo.findAll();
        if (specialists.isEmpty()) {
            throw new ValidationException("No specialists found");
        }
        return specialists;    }

    @Override
    public List<Specialist> getSpecialistByStatus(SpecialistStatus status) {
        return specialistRepo.getSpecialistBySpecialistStatus(status);
    }




    @Override
    public List<BaseUser> getAllUsers() {
        return List.of();
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

        // Verify specialist status here if necessary (e.g., checking if approved)

        org.example.homeservice.entites.Service foundService = serviceRepo.findById(subServiceId)
                .orElseThrow(() -> new ValidationException("Service not found"));

        // Logic to add specialist to the sub-service
//        serviceRepo.addingSpecialistToSubService(specialist, foundService);
        //todo  where is this method!
    }

    private Specialist convertToEntity(SavingSpecialistDTO specialistDTO) {
        Specialist specialist = new Specialist();

        // Set fields from the DTO
        specialist.setFirstName(specialistDTO.getFirstName());
        specialist.setLastName(specialistDTO.getLastName());
        specialist.setEmail(specialistDTO.getEmail());

        // Hash the password before saving
        specialist.setPassword(hashPassword(specialistDTO.getPassword()));
        specialist.setSpecialistStatus(specialistDTO.getSpecialistStatus());
        specialist.setRate(specialistDTO.getRate());
        specialist.setPersonalImage(specialistDTO.getPersonalImage());

        // Convert work service IDs to Service entities if necessary
        List<org.example.homeservice.entites.Service> services = specialistDTO.getWorkServiceIds().stream()
                .map(id -> serviceRepo.findById(id)
                        .orElseThrow(() -> new ValidationException("Service not found")))
                .collect(Collectors.toList());
        specialist.setWorkServices(services);

        return specialist;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
