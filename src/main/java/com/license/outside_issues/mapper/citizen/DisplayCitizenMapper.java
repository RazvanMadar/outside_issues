package com.license.outside_issues.mapper.citizen;

import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DisplayCitizenMapper {
    DisplayCitizenMapper INSTANCE = Mappers.getMapper(DisplayCitizenMapper.class);
    DisplayCitizenDTO modelToDto(Citizen citizen);
    Citizen dtoToModel(DisplayCitizenDTO citizenDTO);
}
