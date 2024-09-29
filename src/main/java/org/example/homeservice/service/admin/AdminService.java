package org.example.homeservice.service.admin;

import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.entites.Admin;
import org.example.homeservice.entites.BaseUser;
import org.example.homeservice.entites.Specialist;
import org.example.homeservice.entites.enums.SpecialistStatus;
import org.example.homeservice.service.baseentity.BaseEntityService;

import java.util.List;

public interface AdminService extends BaseEntityService<Admin, Long, SpecialistRequest, SpecialistResponse> {
    void saveSpecialist(SpecialistRequest specialistDTO);
    void deleteSpecialistById(Long specialistId);
    List<SpecialistResponse> getAllSpecialists();
    List<SpecialistResponse> getSpecialistsByStatus(SpecialistStatus status);
    List<BaseUser> getAllUsers();
    void acceptSpecialist(Long specialistId);
    void addingSpecialistToSubService(Long specialistId, Long subServiceId);
}