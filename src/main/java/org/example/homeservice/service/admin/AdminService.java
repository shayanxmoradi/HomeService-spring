package org.example.homeservice.service.admin;

import org.example.homeservice.dto.service.ServiceRequest;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.specialist.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.domain.user.BaseUser;
import org.example.homeservice.domain.enums.SpecialistStatus;

import java.util.List;
import java.util.Optional;

public interface AdminService  {
    void saveSpecialist(SpecialistRequest specialistDTO);
    void deleteSpecialistById(Long specialistId);
    List<SpecialistResponse> getAllSpecialists();
    List<SpecialistResponse> getSpecialistsByStatus(SpecialistStatus status);
    List<BaseUser> getAllUsers();
    void acceptSpecialist(Long specialistId);
    void addingSpecialistToSubService(Long specialistId, Long subServiceId);
    void deleteSpecialistFromSubService(Long specialistId, Long subServiceId);
     Optional<ServiceResponse> createNewService(ServiceRequest dto);
    Optional<SpecialistResponse> addSpeciliast(SpecialistRequest request);
    void deleteSpecialist(Long specialistId);

     Optional<List<ServiceResponse>> findAllServices();
}