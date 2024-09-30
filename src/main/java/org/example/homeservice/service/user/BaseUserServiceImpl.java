package org.example.homeservice.service.user;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.UpdatePasswordRequst;
import org.example.homeservice.entites.BaseUser;
import org.example.homeservice.entites.Customer;
import org.example.homeservice.repository.user.BaseUserRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public abstract class BaseUserServiceImpl<T extends BaseUser, R extends BaseUserRepo<T>,D,RDTO>
        extends BaseEntityServiceImpl<T, Long, R,D,RDTO> implements BaseUserService<T,D,RDTO> { // Specify <T>

    private final R userRepo;

    @Autowired
    public BaseUserServiceImpl( R userRepo) {
        super(userRepo);
        this.userRepo = userRepo;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public void updatePassword(UpdatePasswordRequst updatePasswordRequst) {
        T baseUser = baseRepository.findByEmail(updatePasswordRequst.email())
                .orElseThrow(() -> new ValidationException("user with this email not found"));

        if (!baseUser.getPassword().equals(updatePasswordRequst.oldPassword())) {
            throw new ValidationException("Incorrect password");
        }

        baseUser.setPassword(updatePasswordRequst.newPassword());
        baseRepository.save(baseUser);
    }

//    @Override
//    public Optional<RDTO> login(String email, String password) {
//        return userRepo.findByEmailAndPassword(email, password);
//    }

//    @Override
//    public Optional<Specialist> save(SavingSpecialistDTO specialistDTO) {
//        if (!emailExists(specialistDTO.getEmail())) {
//            Specialist specialist = convertToEntity(specialistDTO);
//            return Optional.of(baseRepository.save(specialist));
//        }
//        throw new ValidationException("Specialist email already exists");
//    }

//    @Override
//    public LoginResponeDTO login(LoginInDto loginDTO) {
//        Optional<T> optionalSpecialist = login(loginDTO.getEmail(), loginDTO.getPassword());
//
//        if (optionalSpecialist.isEmpty()) {
//            throw new ValidationException("User not found or invalid password");
//        }
//
//        T specialist = optionalSpecialist.get();
//        // Create and return the response DTO
//        LoginResponeDTO response = new LoginResponeDTO();
//        response.setUserId(specialist.getId());
//        response.setFirstName(specialist.getFirstName());
//        response.setLastName(specialist.getLastName());
//
//        return response;
//    }

//    private String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }
//
//    private boolean verifyPassword(String providedPassword, String storedHashedPassword) {
//        return BCrypt.checkpw(providedPassword, storedHashedPassword);
//    }

//    private Specialist convertToEntity(SavingSpecialistDTO specialistDTO) {
//        Specialist specialist = new Specialist();
//
//        // Set fields from the DTO
//        specialist.setFirstName(specialistDTO.getFirstName());
//        specialist.setLastName(specialistDTO.getLastName());
//        specialist.setEmail(specialistDTO.getEmail());
//
//        // Hash the password before saving
//        specialist.setPassword(hashPassword(specialistDTO.getPassword()));
//        specialist.setSpecialistStatus(specialistDTO.getSpecialistStatus());
//        specialist.setRate(specialistDTO.getRate());
//        specialist.setPersonalImage(specialistDTO.getPersonalImage());
//
//        // Convert work service IDs to Service entities if necessary
//        List<org.example.homeservice.entites.Service> services = specialistDTO.getWorkServiceIds().stream()
//                .map(id -> serviceRepo.findById(id)
//                        .orElseThrow(() -> new ValidationException("Service not found")))
//                .collect(Collectors.toList());
//        specialist.setWorkServices(services);
//
//        return specialist;
//    }
}