package org.example.homeservice.repository.service;

import org.example.homeservice.domain.service.Service;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepo extends BaseEnitityRepo<Service, Long>, CustomServiceRepo {
    Optional<Service> findByName(String name);
    List<Service> findAllByParentService(Service parentService); // Change from findAllByParentId to findAllByParentService
    @Query("SELECT s FROM Service s WHERE s.parentService IS NULL")
    List<Service> findFirstLayerServices();
    List<Service> findAll();
    List<Service>findByParentServiceIsNull();// check id null
    List<Service> findByCategoryFalse();
    List<Service> findAllByParentServiceIsNotNull();

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
           "FROM Service srv JOIN srv.avilableSpecialists s " +
           "WHERE srv.id = :serviceId AND s.id = :specialistId")
    boolean isSpecialistAvailableInService(@Param("serviceId") Long serviceId, @Param("specialistId") Long specialistId);


//native query
//
//    @Query(value = "SELECT COUNT(*) > 0 FROM spring.home_service.service_avilable_specialists WHERE service_id = :serviceId AND avilable_specialists_id = :specialistId", nativeQuery = true)
//    boolean isSpecialistAvailableInService(@Param("serviceId") Long serviceId, @Param("specialistId") Long specialistId);

    @Query(value = "SELECT COUNT(*) FROM spring.home_service.service_avilable_specialists WHERE service_id = :serviceId AND avilable_specialists_id = :specialistId", nativeQuery = true)
    int countSpecialistsInService(@Param("serviceId") Long serviceId, @Param("specialistId") Long specialistId);
}