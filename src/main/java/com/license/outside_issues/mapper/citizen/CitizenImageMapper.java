package com.license.outside_issues.mapper.citizen;

import com.license.outside_issues.model.CitizenImage;
import com.license.outside_issues.service.issue.dtos.IssueImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CitizenImageMapper {
    CitizenImageMapper INSTANCE = Mappers.getMapper(CitizenImageMapper.class);
    IssueImageDTO modelToDto(CitizenImage image);
    CitizenImage dtoToModel(IssueImageDTO image);
}