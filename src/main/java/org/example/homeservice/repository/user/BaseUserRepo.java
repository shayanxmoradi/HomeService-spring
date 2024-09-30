package org.example.homeservice.repository.user;

import org.example.homeservice.entites.BaseUser;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseUserRepo<T extends BaseUser> extends BaseEnitityRepo<T, Long> {
    Optional<T> findByEmail(String email);
   // boolean existsByEmail(String email);

  //  T findByUsername(String username);

    Optional<T> findByEmailAndPassword(String email, String password);

}
