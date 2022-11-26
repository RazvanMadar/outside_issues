package com.license.outside_issues.mapper.citizen;

import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegisterCitizenMapper {
    RegisterCitizenMapper INSTANCE = Mappers.getMapper(RegisterCitizenMapper.class);
    RegisterCitizenDTO modelToDto(Citizen citizen);
    Citizen dtoToModel(RegisterCitizenDTO citizenDTO);
}
