package org.example.homeservice.dto.mapper;

import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.domain.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "category",source = "isCategory")
//    @Mapping(target = "parentService.id",source = "parentServiceId")
    Service toEntity(ServiceRequest serviceRequestDto);
//
//    @Mapping(target = "parentServiceId", source = "parentService.id")
//    @Mapping(target = "availableSpecialists", source = "avilableSpecialists")
//    @Mapping(target = "subServices", source = "subServices")
@Mapping(target = "parentServiceId",source = "parentService.id")
ServiceResponse toDto(Service service);

    List<ServiceResponse> toDto(List<Service> service);

    //    @Mapping(target = "isCategory", source = "isCategory")
    ServiceRequest toDtoReq(Service service);

    ServiceRequest responeToDtoReq(ServiceResponse serviceResponse);


//    default List<Long> mapAvailableSpecialists(List<Specialist> availableSpecialists) {
//        if (availableSpecialists == null) {
//            return null;
//        }
//        return availableSpecialists.stream()
//                .map(Specialist::getId)  // Assuming `Specialist` has an `id` field
//                .collect(Collectors.toList());
//    }
//
//    // Recursively map subservices
//    default List<ServiceResponse> mapSubServices(List<Service> subServices) {
//        if (subServices == null) {
//            return null;
//        }
//        return subServices.stream()
//                .map(this::toDto)  // Reusing the `toResponse` method for subservices
//                .collect(Collectors.toList());
//    }

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

//    }