package org.example.homeservice.dto.specialist;

import org.example.homeservice.dto.service.SpecialistRequest;
import org.example.homeservice.domain.Specialist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialistMapper {
    SpecialistMapper INSTANCE = Mappers.getMapper(SpecialistMapper.class);

    Specialist toEntity(SpecialistRequest request);
    @Mapping(target = "wallet.id",source = "walletId")

    Specialist toEntity(SpecialistResponse request);

    //@Mapping(target = "id", source = "entity.id")
    @Mapping(target = "walletId",source = "wallet.id")
    SpecialistResponse toDto(Specialist entity);
  SpecialistRequest toDtoReq(Specialist entity);
    List<SpecialistResponse> toDto(List<Specialist> specialists);



}