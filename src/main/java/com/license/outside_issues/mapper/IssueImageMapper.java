package com.license.outside_issues.mapper;

import com.license.outside_issues.model.IssueImage;
import com.license.outside_issues.service.issue.dtos.IssueImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IssueImageMapper {
    IssueImageMapper INSTANCE = Mappers.getMapper(IssueImageMapper.class);
    IssueImageDTO modelToDto(IssueImage image);
    IssueImage dtoToModel(IssueImageDTO image);
}
