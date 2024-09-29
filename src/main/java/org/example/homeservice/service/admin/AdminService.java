package org.example.homeservice.service.admin;

import org.example.homeservice.entites.Admin;
import org.example.homeservice.entites.BaseUser;
import org.example.homeservice.entites.Specialist;
import org.example.homeservice.entites.enums.SpecialistStatus;
import org.example.homeservice.service.baseentity.BaseEntityService;

import java.util.List;

public interface AdminService<D,RDTO> extends BaseEntityService<Admin,Long,D,RDTO> {
    public void saveSpecialist(SavingSpecialistDTO specialistDTO);

    public void deleteSpcialistById(Long specialistId);

    public List<Specialist> getAllSpecialist();

    public List<Specialist> getSpecialistByStatus(SpecialistStatus status);

    public List<BaseUser> getAllUsers();

    public void acceptSpecialist(Long SpecialistId);

    public void addingSpecialistToSubService(Long specialistId, Long subServiceId);
}
