package com.license.outside_issues.service.blacklist;

public interface BlacklistService {
    Long addCitizenToBlacklist(Long id);
    boolean isCitizenBlocked(Long id);
    Long deleteCitizenFromBlacklist(Long id);
}
