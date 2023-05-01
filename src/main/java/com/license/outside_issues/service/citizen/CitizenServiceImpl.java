package com.license.outside_issues.service.citizen;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.mapper.citizen.DisplayCitizenMapper;
import com.license.outside_issues.mapper.citizen.RegisterCitizenMapper;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.Role;
import com.license.outside_issues.repository.CitizenJdbcRepository;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.repository.RoleRepository;
import com.license.outside_issues.service.citizen.dtos.ChatCitizenDTO;
import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CitizenServiceImpl implements CitizenService {
    private final CitizenRepository citizenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CitizenJdbcRepository citizenJdbcRepository;

    public CitizenServiceImpl(CitizenRepository citizenRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, CitizenJdbcRepository citizenJdbcRepository) {
        this.citizenRepository = citizenRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.citizenJdbcRepository = citizenJdbcRepository;
    }

    @Override
    public Page<DisplayCitizenDTO> getAllCitizens(String email, Pageable pageable) {
        return citizenJdbcRepository.findCitizens(email, pageable);
//        return email.isBlank() || email.isEmpty() ? citizenRepository.findAll().stream()
//                    .map(this::convertCitizenToDTO)
//                    .collect(Collectors.toList()) :
//        citizenRepository.findByEmailContainingIgnoreCase(email).stream()
//                .map(this::convertCitizenToDTO)
//                .collect(Collectors.toList());
    }

    @Override
    public Long registerCitizen(RegisterCitizenDTO citizenDTO) {
        String rawPassword = citizenDTO.getPassword();
        String encodedPassword = rawPassword != null ? passwordEncoder.encode(rawPassword) : null;
        Citizen citizen = RegisterCitizenMapper.INSTANCE.dtoToModel(citizenDTO);
        citizen.setPassword(encodedPassword);
        Set<Role> roles = new HashSet<>();
        Role roleById = roleRepository.findById(2).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ROLE_NOT_FOUND);
        });
        roles.add(roleById);
        citizen.setRoles(roles);
        citizenRepository.save(citizen);
        return citizen.getId();
    }

    @Override
    public DisplayCitizenDTO findByEmail(String email) {
        final Citizen citizen = citizenRepository.findByEmail(email).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        return DisplayCitizenMapper.INSTANCE.modelToDto(citizen);
    }

    @Override
    public DisplayCitizenDTO findById(Long id) {
        Citizen citizenById = citizenRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        return DisplayCitizenMapper.INSTANCE.modelToDto(citizenById);
    }

    @Override
    public List<ChatCitizenDTO> getChatUsersByRole(String role) {
        return citizenRepository.getChatUsers(role).stream()
                .map(this::mapCitizenToChatCitizen)
                .collect(Collectors.toList());
    }

    @Override
    public Long updateCitizen(DisplayCitizenDTO citizen) {
        Citizen citizenById = citizenRepository.findById(citizen.getId()).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        if (citizen.getFirstName() != null) {
            citizenById.setFirstName(citizen.getFirstName());
        }
        if (citizen.getLastName() != null) {
            citizenById.setLastName(citizen.getLastName());
        }
        citizenRepository.save(citizenById);
        return citizenById.getId();
    }

    private ChatCitizenDTO mapCitizenToChatCitizen(Citizen citizen) {
        return new ChatCitizenDTO(citizen.getId(), citizen.getFirstName(), citizen.getLastName(), citizen.getEmail(),
                citizen.getCitizenImage() != null ? citizen.getCitizenImage().getImage(): null);
    }
}
