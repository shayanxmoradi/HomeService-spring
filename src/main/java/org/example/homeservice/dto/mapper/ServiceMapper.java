package org.example.homeservice.dto.mapper;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.entity.Service;
import org.example.homeservice.entity.Specialist;
import org.example.homeservice.repository.service.ServiceRepo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Service toEntity(ServiceRequest serviceRequestDto);

    ServiceResponse toDto(Service service);
    List<ServiceResponse> toDto(List<Service> service);

    //    @Mapping(target = "isCategory", source = "isCategory")
    ServiceRequest toDtoReq(Service service);

    ServiceRequest responeToDtoReq(ServiceResponse serviceResponse);

}



//    @Mapping(target = "parentService", source = "parentServiceId", qualifiedByName = "mapParentServiceById")
//    @Mapping(target = "avilableSpecialists", ignore = true) // Handle separately in the service layer
//    Service toEntity(ServiceRequest serviceRequest);
//
//    @Mapping(source = "parentService.id", target = "parentServiceId")
//    @Mapping(source = "avilableSpecialists", target = "availableSpecialistsIds", qualifiedByName = "mapSpecialistsToIds")
//    ServiceResponse toDto(Service service);
//
//    // Custom methods for mapping IDs
//    @Named("mapSpecialistsToIds")
//    default List<Long> map(List<Specialist> specialists) {
//        return specialists.stream()
//                .map(Specialist::getId)
//                .toList();
//    }
//
//    @Named("mapParentServiceById")
//    default Service mapParentServiceById(Long parentServiceId, @Context ServiceRepo serviceRepository) {
//        if (parentServiceId == null) {
//            return null;
//        }
//        return serviceRepository.findById(parentServiceId)
//                .orElseThrow(() -> new ValidationException("Parent service not found"));
//    }
//
//    }