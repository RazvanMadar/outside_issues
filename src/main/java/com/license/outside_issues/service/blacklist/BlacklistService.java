package com.license.outside_issues.service.blacklist;

import com.license.outside_issues.dto.StatisticsDTO;

import java.util.List;

public interface BlacklistService {
    Long addCitizenToBlacklist(Long id);
    boolean isCitizenBlocked(Long id);
    Long deleteCitizenFromBlacklist(Long id);
    List<StatisticsDTO> getBasicStatistics();
}
