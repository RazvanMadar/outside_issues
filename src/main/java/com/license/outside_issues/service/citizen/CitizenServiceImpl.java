package com.license.outside_issues.service.citizen;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.mapper.citizen.DisplayCitizenMapper;
import com.license.outside_issues.mapper.citizen.RegisterCitizenMapper;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitizenServiceImpl implements CitizenService {
    private final CitizenRepository citizenRepository;

    public CitizenServiceImpl(CitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    @Override
    public List<DisplayCitizenDTO> getAllCitizens() {
        return convertCitizensToDTOS(citizenRepository.findAll());
    }

    @Override
    public Long registerCitizen(RegisterCitizenDTO citizenDTO) {
        Citizen citizen = RegisterCitizenMapper.INSTANCE.dtoToModel(citizenDTO);
        citizenRepository.save(citizen);
        return citizen.getId();
    }

    @Override
    public DisplayCitizenDTO findById(Long id) {
        Citizen citizenById = citizenRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        return DisplayCitizenMapper.INSTANCE.modelToDto(citizenById);
    }

    private List<DisplayCitizenDTO> convertCitizensToDTOS(List<Citizen> citizens) {
        return citizens.stream()
                .map(DisplayCitizenMapper.INSTANCE::modelToDto)
                .collect(Collectors.toList());
    }
}
