package org.example.homeservice.repository.service;

import org.example.homeservice.domain.service.Service;

public interface CustomServiceRepo {
    boolean addSubService(Long parentId, Service subService);
}