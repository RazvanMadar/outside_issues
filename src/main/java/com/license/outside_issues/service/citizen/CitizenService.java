package com.license.outside_issues.service.citizen;

import com.license.outside_issues.service.citizen.dtos.ChatCitizenDTO;
import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CitizenService {
    Page<DisplayCitizenDTO> getAllCitizens(String email, boolean isFiltered, Pageable pageable);
    Long registerCitizen(RegisterCitizenDTO citizen);
    DisplayCitizenDTO findByEmail(String email);
    DisplayCitizenDTO findById(Long id);
    List<ChatCitizenDTO> getChatUsersByRole(String role);
    Long updateCitizen(DisplayCitizenDTO citizen);
    List<DisplayCitizenDTO> findAllCitizenUsers();
}
