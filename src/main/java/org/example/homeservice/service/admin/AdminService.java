package org.example.homeservice.service.admin;

import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.dto.SpecialistRequest;
import org.example.homeservice.dto.SpecialistResponse;
import org.example.homeservice.entity.BaseUser;
import org.example.homeservice.entity.enums.SpecialistStatus;

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
     Optional<ServiceResponse> createNewService(ServiceRequest dto);
    Optional<SpecialistResponse> addSpeciliast(SpecialistRequest request);
    void deleteSpecialist(Long specialistId);

     Optional<List<ServiceResponse>> findAllServices();
}