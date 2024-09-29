package org.example.dto;

import org.example.entites.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {


    @Mapping(source = "parentService.id", target = "parentServiceId")
    CreateServiceRequest toCreateServiceRequest(Service service);



    @Mapping(target = "id",source = "id")
    Service toEntity(AddServiceDto dto);

//    Re toRequstDto(Service service);
}
