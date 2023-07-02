package com.license.outside_issues.service.citizen;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.Role;
import com.license.outside_issues.repository.CitizenJdbcRepository;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.repository.RoleRepository;
import com.license.outside_issues.dto.ChatCitizenDTO;
import com.license.outside_issues.dto.DisplayCitizenDTO;
import com.license.outside_issues.dto.RegisterCitizenDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public Page<DisplayCitizenDTO> getAllCitizens(String email, boolean isFiltered, Pageable pageable) {
        return citizenJdbcRepository.findCitizens(email, isFiltered, pageable);
    }

    @Override
    public Long registerCitizen(RegisterCitizenDTO citizenDTO, boolean isAuth) {
        if (citizenDTO.getEmail() == null) {
            throw new BusinessException(ExceptionReason.BAD_REQUEST);
        }
        final Optional<Citizen> alreadyPresentCitizen = citizenRepository.findAll().stream()
                .filter(citizen -> citizen.getEmail().equals(citizenDTO.getEmail()))
                .findAny();
        if (alreadyPresentCitizen.isPresent() && alreadyPresentCitizen.get().getPassword() != null) {
            throw new BusinessException(ExceptionReason.CITIZEN_EXISTS);
        }

        if (!isAuth) {
            if (alreadyPresentCitizen.isPresent()) {
                return alreadyPresentCitizen.get().getId();
            }
        }
        if (alreadyPresentCitizen.isPresent() && alreadyPresentCitizen.get().getPassword() == null) {
            Citizen citizen = alreadyPresentCitizen.get();
            String rawPassword = citizenDTO.getPassword();
            String encodedPassword = rawPassword != null ? passwordEncoder.encode(rawPassword) : null;
            citizen.setPassword(encodedPassword);
            citizen.setPhoneNumber(citizenDTO.getPhoneNumber());
            citizen.setFirstName(citizenDTO.getFirstName());
            citizen.setLastName(citizenDTO.getLastName());
            final Citizen savedUser = citizenRepository.save(citizen);
            return savedUser.getId();
        }
        else {
            String rawPassword = citizenDTO.getPassword();

            String encodedPassword = rawPassword != null ? passwordEncoder.encode(rawPassword) : null;
            Citizen citizen = new Citizen(citizenDTO);
            citizen.setPassword(encodedPassword);
            Set<Role> roles = new HashSet<>();
            Optional<Role> roleById = roleRepository.findById(1L);
            if (roleById.isEmpty()) {
                final Role roleAdmin = roleRepository.save(new Role("ROLE_ADMIN"));
                roles.add(roleAdmin);
                citizen.setRoles(roles);
                final Citizen savedUser = citizenRepository.save(citizen);
                return savedUser.getId();
            }
            roleById = roleRepository.findById(2L);
            if (roleById.isEmpty()) {
                final Role roleUser = roleRepository.save(new Role("ROLE_USER"));
                roles.add(roleUser);
                citizen.setRoles(roles);
                final Citizen savedUser = citizenRepository.save(citizen);
                return savedUser.getId();
            }
            Role role = roleRepository.findById(2L).orElseThrow(() -> {
                throw new BusinessException(ExceptionReason.ROLE_NOT_FOUND);
            });
            roles.add(role);
            citizen.setRoles(roles);
            final Citizen savedUser = citizenRepository.save(citizen);
            return savedUser.getId();
        }
    }

    @Override
    public DisplayCitizenDTO findByEmail(String email) {
        final Citizen citizen = citizenRepository.findByEmail(email).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
//        return DisplayCitizenMapper.INSTANCE.modelToDto(citizen);
        return new DisplayCitizenDTO(citizen);
    }

    @Override
    public DisplayCitizenDTO findById(Long id) {
        Citizen citizenById = citizenRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
//        return DisplayCitizenMapper.INSTANCE.modelToDto(citizenById);
        return new DisplayCitizenDTO(citizenById);
    }

    @Override
    public List<ChatCitizenDTO> findByName(String name) {
        return citizenRepository.findAllCitizenUsers().stream()
                .filter(citizen -> citizen.getLastName().toLowerCase().contains(name.toLowerCase()) || citizen.getFirstName().toLowerCase().contains(name.toLowerCase()))
                .map(this::mapCitizenToChatCitizen)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatCitizenDTO> getChatUsersByRole(String role, String searchPerson) {
        if ("ROLE_ADMIN".equals(role)) {
            if (searchPerson.length() > 0) {
                return findByName(searchPerson);
            }
            return citizenRepository.getChatUsersForAdmin().stream()
                    .map(this::mapCitizenToChatCitizen)
                    .collect(Collectors.toList());
        }
        return citizenRepository.getChatUsersForCitizen().stream()
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
        final Citizen updatedCitizen = citizenRepository.save(citizenById);
        return updatedCitizen.getId();
    }

    @Override
    public List<DisplayCitizenDTO> findAllCitizenUsers() {
        return citizenRepository.findAllCitizenUsers().stream()
                .map(DisplayCitizenDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllUsersExceptOne(String email) {
        return citizenRepository.findAll().stream()
                .map(Citizen::getEmail)
                .filter(citizenEmail -> !citizenEmail.equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllValidEmails() {
        return citizenRepository.findAll().stream()
                .filter(citizen -> citizen.getPassword() != null)
                .map(Citizen::getEmail)
                .collect(Collectors.toList());
    }

    private ChatCitizenDTO mapCitizenToChatCitizen(Citizen citizen) {
        return new ChatCitizenDTO(citizen.getId(), citizen.getFirstName(), citizen.getLastName(), citizen.getEmail(),
                citizen.getCitizenImage() != null ? citizen.getCitizenImage().getImage() : null);
    }
}
