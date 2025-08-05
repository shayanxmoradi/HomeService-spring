package org.example.homeservice.service.user;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.updatepassword.UpdatePasswordRequst;
import org.example.homeservice.domain.user.BaseUser;
import org.example.homeservice.repository.user.BaseUserRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public abstract class BaseUserServiceImpl<T extends BaseUser, R extends BaseUserRepo<T>, D, RDTO>
        extends BaseEntityServiceImpl<T, Long, R, D, RDTO> implements BaseUserService<T, D, RDTO> { // Specify <T>

    private final R userRepo;

    @Autowired
    public BaseUserServiceImpl(R userRepo) {
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


}