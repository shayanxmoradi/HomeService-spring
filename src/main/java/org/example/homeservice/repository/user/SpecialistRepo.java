package org.example.homeservice.repository.user;

import org.example.homeservice.domain.Specialist;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("specialistRepo")

public interface SpecialistRepo extends BaseUserRepo<Specialist> , JpaSpecificationExecutor<Specialist> {
    List<Specialist> getSpecialistBySpecialistStatus(SpecialistStatus specialistStatus);

}
