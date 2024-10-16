package org.example.homeservice.repository.user;

import org.example.homeservice.domain.BaseUser;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseUserRepo<T extends BaseUser> extends BaseEnitityRepo<T, Long>  {
    Optional<T> findByEmail(String email);
    boolean existsByEmail(String email);
   // boolean existsByEmail(String email);

  //  T findByUsername(String username);

    Optional<T> findByEmailAndPassword(String email, String password);

}
