package com.license.outside_issues.mapper;

import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IssueMapper {
    IssueMapper INSTANCE = Mappers.getMapper(IssueMapper.class);
    IssueDTO modelToDto(Issue issue);
    Issue dtoToModel(IssueDTO issueDTO);
}
