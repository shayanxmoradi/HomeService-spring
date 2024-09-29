package org.example.homeservice.dto;

import org.example.homeservice.entites.Specialist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialistMapper {
    SpecialistMapper INSTANCE = Mappers.getMapper(SpecialistMapper.class);

    Specialist toEntity(SpecialistRequest request);

    //@Mapping(target = "id", source = "entity.id")
    SpecialistResponse toDto(Specialist entity);
    List<SpecialistResponse> toDto(List<Specialist> specialists);
}