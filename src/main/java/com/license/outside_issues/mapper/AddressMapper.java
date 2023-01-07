package com.license.outside_issues.mapper;

import com.license.outside_issues.model.Address;
import com.license.outside_issues.service.issue.dtos.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    AddressDTO modelToDto(Address address);
    Address dtoToModel(AddressDTO addressDTO);
}
