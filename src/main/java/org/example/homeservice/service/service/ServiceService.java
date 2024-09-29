package org.example.services.service;

import org.example.dto.AddServiceDto;
import org.example.entites.Service;
import org.example.services.baseentity.BaseEnitityServce;

import java.util.List;
import java.util.Optional;

public interface ServiceService extends BaseEnitityServce<Service,Long> {
    Optional<Service> findByName(String name);
    boolean addSubService(AddServiceDto addServiceDto);
    boolean removeSubService(Long serviceId);
    List<Service> findAllByParentId(Long parentId);

}
