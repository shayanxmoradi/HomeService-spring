package org.example.services.service;

import jakarta.validation.ValidationException;
import org.example.dto.AddServiceDto;
import org.example.entites.Service;
import org.example.repositories.service.ServiceRepo;
import org.example.services.baseentity.BaseEntityServceImpl;

import java.util.List;
import java.util.Optional;

public class ServiceServiceImpl extends BaseEntityServceImpl<Service,Long,ServiceRepo> implements ServiceService {
    ServiceRepo serviceRepo;



    public ServiceServiceImpl(ServiceRepo baseRepo) {
        super(baseRepo);
        this.serviceRepo = baseRepo;
    }


     @Override
     public Optional<Service> findByName(String name) {
         return serviceRepo.findByName(name);
     }


     @Override
     public boolean addSubService(AddServiceDto addServiceDto) {
         //todo should we check real servicde here?

         Optional<Service> byName = serviceRepo.findByName(addServiceDto.getName());
         if (byName.isPresent()) {
             throw  new ValidationException("Service with name " + addServiceDto.getName() + " already exists");
         }

         return serviceRepo.addSubService(addServiceDto.getParentServiceId(), convertToEntity(addServiceDto));
     }

     @Override
     public boolean removeSubService(Long serviceId) {
         return false;
     }

    @Override
    public List<Service> findAllByParentId(Long parentId) {
        return serviceRepo.findAllByParentId(parentId);
    }
    private Service convertToEntity(AddServiceDto serviceDTO) {
        Service service = new Service();
        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setBasePrice(serviceDTO.getBasePrice());
        service.setParentServiceById(null, serviceDTO.getParentServiceId()); //todo haa?
        service.setCategory(serviceDTO.isCategory());
        return service;
    }
}
