package com.license.outside_issues.issues;

import com.license.outside_issues.repository.CitizenReactionsRepository;
import com.license.outside_issues.repository.IssueImageRepository;
import com.license.outside_issues.repository.IssueJdbcRepository;
import com.license.outside_issues.repository.IssueRepository;
import com.license.outside_issues.service.issue.IssueService;
import com.license.outside_issues.service.issue.IssueServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
    public void test_insertM3RGEBUSRUsers_withoutNewUser() {
        System.out.println("Merg testele!");
    }
}
