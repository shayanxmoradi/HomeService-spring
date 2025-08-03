package org.example.homeservice.repository.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.homeservice.domain.service.Service;

public class CustomServiceRepoImpl implements CustomServiceRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean addSubService(Long parentId, Service subService) {
        // Fetch the parent service
        Service parentService = entityManager.find(Service.class, parentId);
        if (parentService != null) {
            parentService.addSubService(subService);  // Assuming this method exists in Service class
            entityManager.persist(parentService);
            return true;
        }
        return false;
    }
}