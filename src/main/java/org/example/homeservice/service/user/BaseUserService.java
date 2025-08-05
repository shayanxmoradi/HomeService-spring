package org.example.homeservice.service.user;

import org.example.homeservice.dto.updatepassword.UpdatePasswordRequst;
import org.example.homeservice.domain.user.BaseUser;
import org.example.homeservice.service.baseentity.BaseEntityService;

import java.util.Optional;

public interface BaseUserService<T extends BaseUser, D, RDTO> extends BaseEntityService<T, Long, D, RDTO> {
    boolean emailExists(String email);

    Optional<RDTO> login(String email, String password);

    public void updatePassword(UpdatePasswordRequst updatePasswordRequst);

}
