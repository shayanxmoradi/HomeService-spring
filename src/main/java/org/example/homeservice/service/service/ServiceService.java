package org.example.homeservice.service.service;


import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.entity.Service;
import org.example.homeservice.service.baseentity.BaseEntityService;

import java.util.List;
import java.util.Optional;

public interface ServiceService extends BaseEntityService<Service,Long, ServiceRequest, ServiceResponse> {
    Optional<ServiceResponse> findByName(String name);
    List<ServiceResponse> findAllByParentId(Long parentId);

    List<ServiceResponse> findRealServices();
}
