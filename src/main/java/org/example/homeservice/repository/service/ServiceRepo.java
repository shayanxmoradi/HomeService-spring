package org.example.homeservice.repository.service;

import org.example.homeservice.entites.Service;
import org.example.homeservice.entites.Specialist;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepo extends BaseEnitityRepo<Service, Long>, CustomServiceRepo {
    Optional<Service> findByName(String name);
    List<Service> findAllByParentService(Service parentService); // Change from findAllByParentId to findAllByParentService
    @Query("SELECT s FROM Service s WHERE s.parentService IS NULL")
    List<Service> findFirstLayerServices();
}