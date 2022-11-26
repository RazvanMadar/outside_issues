package com.license.outside_issues.service.citizen;

import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;

import java.util.List;


public interface CitizenService {
    List<DisplayCitizenDTO> getAllCitizens();
    Long registerCitizen(RegisterCitizenDTO citizen);
    DisplayCitizenDTO findById(Long id);
}
