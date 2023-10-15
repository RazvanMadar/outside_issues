package com.license.outside_issues;

import com.license.outside_issues.dto.AddressDTO;
import com.license.outside_issues.dto.IssueDTO;
import com.license.outside_issues.dto.StatisticsDTO;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.model.Address;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.repository.CitizenReactionsRepository;
import com.license.outside_issues.repository.IssueImageRepository;
import com.license.outside_issues.repository.IssueJdbcRepository;
import com.license.outside_issues.repository.IssueRepository;
import com.license.outside_issues.service.issue.IssueServiceImpl;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
    @InjectMocks
    private IssueServiceImpl issueService;
    @Mock
    private IssueRepository issueRepository;
    @Mock
    private IssueJdbcRepository issueJdbcRepository;
    @Mock
    private CitizenReactionsRepository citizenReactionsRepository;
    @Mock
    private IssueImageRepository issueImageRepository;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void test_findIssues_all() {
        Pageable pageable = PageRequest.of(0, 3);
        IssueDTO issueDTO1 = new IssueDTO();
        IssueDTO issueDTO2 = new IssueDTO();
        IssueDTO issueDTO3 = new IssueDTO();
        List<IssueDTO> issues = List.of(issueDTO1, issueDTO2, issueDTO3);
        final Page<IssueDTO> page = new PageImpl<>(issues);

        when(issueService.findIssues(null, null, null, null, false, true, pageable))
                .thenReturn(page);
        Page<IssueDTO> result = issueService.findIssues(null, null, null, null, false, true, pageable);

        assertEquals(result.getTotalElements(), 3);
    }

    @Test
    public void test_findIssues_filteredByState() {
        Pageable pageable = PageRequest.of(0, 3);
        IssueDTO issueDTO1 = new IssueDTO();
        issueDTO1.setState(IssueState.REGISTERED);
        IssueDTO issueDTO2 = new IssueDTO();
        issueDTO2.setState(IssueState.SOLVED);
        IssueDTO issueDTO3 = new IssueDTO();
        issueDTO3.setState(IssueState.SOLVED);
        List<IssueDTO> issues = List.of(issueDTO2, issueDTO3);
        final Page<IssueDTO> page = new PageImpl<>(issues);

        when(issueService.findIssues(null, "SOLVED", null, null, false, true, pageable))
                .thenReturn(page);
        Page<IssueDTO> result = issueService.findIssues(null, "SOLVED", null, null, false, true, pageable);

        assertEquals(result.getTotalElements(), 2);
    }

    @Test
    public void test_addIssue() {
        IssueDTO issueDTO = new IssueDTO(IssueType.TRAFFIC_ROAD_SIGNS, new AddressDTO(), IssueState.REGISTERED,
                LocalDate.now(), 0, 0, "Semn de circulatie rupt", true,
                "danpopescu@gmail.com", "Cartier Nufaru");
        Issue issue = new Issue();
        issue.setId(1L);

        when(issueRepository.save(any(Issue.class))).thenReturn(issue);
        Long addedIssueId = issueService.addIssue(issueDTO);

        assertEquals(Optional.of(addedIssueId), Optional.of(1L));
    }

    @Test
    public void test_updateIssue() {
        Long id = 1L;
        String type = "ROAD";
        String state = "WORKING";
        Issue issue = new Issue();
        issue.setType(IssueType.valueOf(type));
        issue.setState(IssueState.REGISTERED);
        Issue savedIssue = new Issue();
        savedIssue.setAddress(new Address());
        savedIssue.setType(IssueType.valueOf(type));
        savedIssue.setState(IssueState.valueOf(state));

        when(issueRepository.findById(id)).thenReturn(Optional.of(issue));
        when(issueRepository.save(issue)).thenReturn(savedIssue);
        final IssueDTO updatedIssue = issueService.updateIssue(id, type, state);

        assertEquals(updatedIssue.getType(), IssueType.ROAD);
        assertEquals(updatedIssue.getState(), IssueState.WORKING);
    }

    @Test
    public void test_deleteIssue() {
        Long id = 1L;
        Issue issue = new Issue();
        issue.setImages(Set.of());
        issue.setCitizenReactions(Set.of());

        when(issueRepository.findById(id)).thenReturn(Optional.of(issue));
        final Long deletedIssueId = issueService.deleteIssue(id);

        assertEquals(Optional.of(deletedIssueId), Optional.of(id));
    }

    @Test
    public void test_getBasicStatistics() {
        StatisticsDTO roadStatistics = new StatisticsDTO("REGISTERED", 1);
        StatisticsDTO plannedStatistics = new StatisticsDTO("PLANNED", 1);
        StatisticsDTO workingStatistics = new StatisticsDTO("WORKING", 1);
        StatisticsDTO solvedStatistics = new StatisticsDTO("SOLVED", 1);
        List<StatisticsDTO> basicStatistics = List.of(roadStatistics, plannedStatistics, workingStatistics, solvedStatistics);

        when(issueService.getBasicStatistics(anyString())).thenReturn(basicStatistics);

        assertEquals(issueService.getBasicStatistics(anyString()).size(), 4);
    }

    @Test
    public void test_getTypeStatistics() {
        StatisticsDTO roadStatistics = new StatisticsDTO("ROAD", 5);
        StatisticsDTO lightningStatistics = new StatisticsDTO("LIGHTNING", 3);
        StatisticsDTO greenSpacesStatistics = new StatisticsDTO("GREEN_SPACES", 4);
        StatisticsDTO publicDomainStatistics = new StatisticsDTO("PUBLIC_DOMAIN", 7);
        StatisticsDTO publicDisorderStatistics = new StatisticsDTO("PUBLIC_DISORDER", 0);
        StatisticsDTO publicTransportStatistics = new StatisticsDTO("PUBLIC_TRANSPORT", 12);
        StatisticsDTO buildingsStatistics = new StatisticsDTO("BUILDINGS", 9);
        StatisticsDTO trafficRoadSignsStatistics = new StatisticsDTO("TRAFFIC_ROAD_SIGNS", 2);
        StatisticsDTO animalsStatistics = new StatisticsDTO("ANIMALS", 1);
        List<StatisticsDTO> basicStatistics = List.of(roadStatistics, lightningStatistics, greenSpacesStatistics, publicDomainStatistics,
                publicDisorderStatistics, publicTransportStatistics, buildingsStatistics, trafficRoadSignsStatistics, animalsStatistics);

        when(issueService.getTypeStatistics(anyString())).thenReturn(basicStatistics);

        assertEquals(issueService.getTypeStatistics(anyString()).size(), 9);
    }

    @Test
    public void test_getYearStatistics() {
        StatisticsDTO statistics1 = new StatisticsDTO("1", 2);
        StatisticsDTO statistics2 = new StatisticsDTO("2", 3);
        StatisticsDTO statistics3 = new StatisticsDTO("3", 4);
        StatisticsDTO statistics4 = new StatisticsDTO("4", 5);
        StatisticsDTO statistics5 = new StatisticsDTO("5", 8);
        StatisticsDTO statistics6 = new StatisticsDTO("6", 15);
        StatisticsDTO statistics7 = new StatisticsDTO("7", 2);
        StatisticsDTO statistics8 = new StatisticsDTO("8", 0);
        StatisticsDTO statistics9 = new StatisticsDTO("9", 0);
        StatisticsDTO statistics10 = new StatisticsDTO("10", 0);
        StatisticsDTO statistics11 = new StatisticsDTO("11", 0);
        StatisticsDTO statistics12 = new StatisticsDTO("12", 0);
        List<StatisticsDTO> basicStatistics = List.of(statistics1, statistics2, statistics3, statistics4,
                statistics5, statistics6, statistics7, statistics8, statistics9, statistics10, statistics11, statistics12);

        when(issueService.getYearStatistics(anyString())).thenReturn(basicStatistics);

        assertEquals(issueService.getYearStatistics(anyString()).size(), 12);
    }

}
