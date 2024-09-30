package org.example.homeservice.service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.example.homeservice.dto.mapper.ServiceMapper;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.entites.Service;
import org.example.homeservice.repository.service.ServiceRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends BaseEntityServiceImpl<Service,Long, ServiceRepo, ServiceRequest, ServiceResponse> implements ServiceService {
    private final ServiceMapper serviceMapper;


    public ServiceServiceImpl(ServiceRepo baseRepository, ServiceMapper serviceMapper) {
        super(baseRepository);
        this.serviceMapper = serviceMapper;
    }

    @Override
    public Optional<ServiceResponse> findByName(String name) {
        return baseRepository.findByName(name)
                .map(serviceMapper::toDto); // Convert Service entity to ServiceResponse
    }

    @Override
    public boolean addSubService(ServiceRequest addServiceDto) {
        // Check for existing service
        Optional<Service> existingService = baseRepository.findByName(addServiceDto.name());
        if (existingService.isPresent()) {
            throw new ValidationException("Service with name " + addServiceDto.name() + " already exists");
        }

        // Convert DTO to entity using ServiceMapper
        Service newService = serviceMapper.toEntity(addServiceDto);

        // Add the new service as a subservice
        return baseRepository.addSubService(addServiceDto.parentServiceId(), newService);
    }

    @Override
    public List<ServiceResponse> findAllByParentId(Long parentId) {
        Optional<Service> parentService = baseRepository.findById(parentId);
        if (parentService.isPresent()) {
            // Convert list of services to ServiceResponse DTOs
            return baseRepository.findAllByParentService(parentService.get())
                    .stream()
                    .map(serviceMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Parent service not found with id: " + parentId);
        }
    }
}
