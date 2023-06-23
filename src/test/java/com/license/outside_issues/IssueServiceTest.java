package com.license.outside_issues;

import com.license.outside_issues.dto.IssueDTO;
import com.license.outside_issues.dto.StatisticsDTO;
import com.license.outside_issues.enums.IssueState;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

        when(issueService.findIssues(null, null, null, null, false, true, pageable)).thenReturn(page);
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

        when(issueService.findIssues(null, "SOLVED", null, null, false, true, pageable)).thenReturn(page);
        Page<IssueDTO> result = issueService.findIssues(null, "SOLVED", null, null, false, true, pageable);

        assertEquals(result.getTotalElements(), 2);
    }

    @Test
    public void test_getBasicStatistics() {
        StatisticsDTO roadStatistics = new StatisticsDTO("REGISTERED", 5);
        StatisticsDTO plannedStatistics = new StatisticsDTO("PLANNED", 3);
        StatisticsDTO workingStatistics = new StatisticsDTO("WORKING", 2);
        StatisticsDTO solvedStatistics = new StatisticsDTO("SOLVED", 0);
        List<StatisticsDTO> basicStatistics = List.of(roadStatistics, plannedStatistics, workingStatistics, solvedStatistics);

        when(issueService.getBasicStatistics(null)).thenReturn(basicStatistics);

        assertEquals(issueService.getBasicStatistics(null).size(), 4);
    }
}
