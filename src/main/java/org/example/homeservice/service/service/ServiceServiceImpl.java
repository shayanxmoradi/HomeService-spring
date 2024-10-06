package org.example.homeservice.service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.example.homeservice.dto.mapper.ServiceMapper;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.domain.Service;
import org.example.homeservice.repository.service.ServiceRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends BaseEntityServiceImpl<Service, Long, ServiceRepo, ServiceRequest, ServiceResponse> implements ServiceService {
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
    public Optional<ServiceResponse> save(ServiceRequest dto) {
        Optional<Service> existingService = baseRepository.findByName(dto.name());

        if (existingService.isPresent()) {
            throw new ValidationException("Service with name " + dto.name() + " already exists");
        }
        Service savingService = serviceMapper.toEntity(dto);

        if (dto.parentServiceId() != null) {
            Optional<Service> parentServiceOpt = baseRepository.findById(dto.parentServiceId()); // Fetch parent service by ID
            if (parentServiceOpt.isPresent()) {
                Service parentService = parentServiceOpt.get();
                if (!parentService.isCategory()) {
                    throw new ValidationException("Parent service with ID " + dto.parentServiceId() + " is not a category and cannot be a parent.");
                }
                savingService.setParentService(parentService);
            } else {
                throw new ValidationException("Parent service with ID " + dto.parentServiceId() + " not found");
            }
        }
        // Convert DTO to entity and save
        return Optional.ofNullable(serviceMapper.toDto(baseRepository.save(savingService)));
    }



    @Override
    public List<ServiceResponse> findAllByParentId(Long parentId) {
        Optional<Service> parentService = baseRepository.findById(parentId);
        if (parentService.isPresent()) {
            return baseRepository.findAllByParentService(parentService.get())
                    .stream()
                    .map(serviceMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Parent service not found with id: " + parentId);
        }
    }

    @Override
    public Optional<List<ServiceResponse>> findAll() {
        List<ServiceResponse> allServices = baseRepository.findAll()
                .stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());

        return Optional.ofNullable(allServices);
    }

//    public List<ServiceResponse> findAllServicesWithParentId() {
//        return baseRepository.findAllByParentServiceIsNotNull()
//                .stream()
//                .map(serviceMapper::toDto)
//                .collect(Collectors.toList());
//    }

    public List<ServiceResponse> findRealServices() {
        return baseRepository.findByCategoryFalse()
                .stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponse> findFirstLayerServices() {
        return baseRepository.findByParentServiceIsNull()
                .stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ServiceResponse> updateService(ServiceResponse serviceResponse) {
      //  baseRepository.save(serviceResponse)
        return Optional.empty();
    }

    @Override
    public Service findByIdX(Long id) {
        return baseRepository.findById(id).get();
    }

    @Override
    protected ServiceResponse toDto(Service entity) {
        return serviceMapper.toDto(entity);
    }

    @Override
    protected Service toEntity(ServiceRequest dto) {
        return serviceMapper.toEntity(dto);
    }


}
