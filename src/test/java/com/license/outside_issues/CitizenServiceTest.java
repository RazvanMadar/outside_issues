package com.license.outside_issues;

import com.license.outside_issues.dto.ChatCitizenDTO;
import com.license.outside_issues.dto.DisplayCitizenDTO;
import com.license.outside_issues.dto.RegisterCitizenDTO;
import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.Role;
import com.license.outside_issues.repository.CitizenJdbcRepository;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.repository.RoleRepository;
import com.license.outside_issues.service.citizen.CitizenServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class CitizenServiceTest {
    @InjectMocks
    private CitizenServiceImpl citizenService;
    @Mock
    private CitizenRepository citizenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CitizenJdbcRepository citizenJdbcRepository;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void test_getAllCitizens() {
        Pageable pageable = PageRequest.of(0, 3);
        DisplayCitizenDTO citizenDTO1 = new DisplayCitizenDTO();
        DisplayCitizenDTO citizenDTO2 = new DisplayCitizenDTO();
        DisplayCitizenDTO citizenDTO3 = new DisplayCitizenDTO();
        List<DisplayCitizenDTO> citizens = List.of(citizenDTO1, citizenDTO2, citizenDTO3);
        final Page<DisplayCitizenDTO> page = new PageImpl<>(citizens);

        when(citizenService.getAllCitizens(null, true, pageable)).thenReturn(page);
        Page<DisplayCitizenDTO> result = citizenService.getAllCitizens(null, true, pageable);

        assertEquals(result.getTotalElements(), 3);
    }

    @Test
    public void test_registerCitizen() {
        RegisterCitizenDTO registerCitizenDTO = new RegisterCitizenDTO("danpopescu@gmail.com", "0712345678", "Dan", "Popescu", "pass1234");
        boolean isAuth = true;
        String encodedPassword = "$2a$10$poZIFVdVE/xiq1Iyo8QHAenZ8JXQ2lH6z96q.IWRzNwqky4kqS9oG";
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        Citizen citizen = new Citizen();
        citizen.setId(1L);

        when(citizenRepository.findAll()).thenReturn(List.of());
        when(passwordEncoder.encode("pass1234")).thenReturn(encodedPassword);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleAdmin));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(roleUser));
        when(citizenRepository.save(any(Citizen.class))).thenReturn(citizen);
        final Long newUserId = citizenService.registerCitizen(registerCitizenDTO, isAuth);

        assertEquals(Optional.of(newUserId), Optional.of(1L));
        assertNotNull(newUserId);
    }

    @Test
    public void test_updateCitizen() {
        DisplayCitizenDTO displayCitizenDTO = new DisplayCitizenDTO(3L, "danpopescu@gmail.com", null, null, "Ionescu", null, null, false);
        Citizen citizen = new Citizen();
        citizen.setId(3L);
        citizen.setFirstName("Dan");
        citizen.setLastName("Popescu");

        when(citizenRepository.findById(3L)).thenReturn(Optional.of(citizen));
        when(citizenRepository.save(any(Citizen.class))).thenReturn(citizen);
        final Long updatedCitizenId = citizenService.updateCitizen(displayCitizenDTO);

        assertEquals(Optional.of(updatedCitizenId), Optional.of(3L));
        assertNotNull(updatedCitizenId);
    }

    @Test
    public void test_findByEmail() {
        String citizenEmail = "not_found@gmail.com";

        assertThrows(BusinessException.class, () -> citizenService.findByEmail(citizenEmail));
    }

    @Test
    public void test_getChatUsersByRole() {
        String roleName = "ROLE_ADMIN";
        Role role = new Role(roleName);
        String searchPerson = "";
        Citizen admin = new Citizen();
        admin.setRoles(Set.of(role));
        admin.setEmail("admin@gmail.com");

        when(citizenRepository.getChatUsersForAdmin()).thenReturn(List.of(admin));
        final List<ChatCitizenDTO> chatUsersByRole = citizenService.getChatUsersByRole(roleName, searchPerson);

        assertEquals(1, chatUsersByRole.size());
        assertEquals(chatUsersByRole.get(0).getEmail(), "admin@gmail.com");
    }

    @Test
    public void test_findAllCitizenUsers() {
        Citizen citizen1 = new Citizen();
        citizen1.setEmail("danpopescu@gmail.com");
        citizen1.setFirstName("Dan");
        citizen1.setLastName("Popescu");
        citizen1.setPhoneNumber("0712345678");
        citizen1.setRoles(Set.of(new Role("ROLE_USER")));
        Citizen citizen2 = new Citizen();
        citizen2.setEmail("danvasilescu@gmail.com");
        citizen2.setFirstName("Dan");
        citizen2.setLastName("Popescu");
        citizen2.setPhoneNumber("0712345678");
        citizen2.setRoles(Set.of(new Role("ROLE_USER")));

        when(citizenRepository.findAllCitizenUsers()).thenReturn(List.of(citizen1, citizen2));
        final List<DisplayCitizenDTO> allCitizenUsers = citizenService.findAllCitizenUsers();

        assertEquals(2, allCitizenUsers.size());
    }

    @Test
    public void test_findAllValidEmails() {
        Citizen citizen1 = new Citizen();
        citizen1.setEmail("danpopescu@gmail.com");
        citizen1.setPassword("$2a$10$poZIFVdVE/xiq1Iyo8QHAenZ8JXQ2lH6z96q.IWRzNwqky4kqS9oG");
        Citizen citizen2 = new Citizen();
        citizen2.setEmail("danvasilescu@gmail.com");
        citizen2.setPassword(null);

        when(citizenRepository.findAll()).thenReturn(List.of(citizen1, citizen2));
        final List<String> allValidEmails = citizenService.findAllValidEmails();

        assertEquals(1, allValidEmails.size());
    }
}
