package org.example.homeservice.repository.user;

import org.example.homeservice.entites.Specialist;
import org.example.homeservice.entites.enums.SpecialistStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("specialistRepo")

public interface SpecialistRepo extends BaseUserRepo<Specialist> {
    List<Specialist> getSpecialistBySpecialistStatus(SpecialistStatus specialistStatus);

}
