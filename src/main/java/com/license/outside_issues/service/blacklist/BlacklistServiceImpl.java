package com.license.outside_issues.service.blacklist;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.Blacklist;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.repository.BlacklistRepository;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlacklistServiceImpl implements BlacklistService {
    private final BlacklistRepository blacklistRepository;
    private final CitizenRepository citizenRepository;

    public BlacklistServiceImpl(BlacklistRepository blacklistRepository, CitizenRepository citizenRepository) {
        this.blacklistRepository = blacklistRepository;
        this.citizenRepository = citizenRepository;
    }

    @Override
    public Long addCitizenToBlacklist(Long id) {
        final Optional<Blacklist> blacklistsByCitizenId = blacklistRepository.findBlacklistsByCitizenId(id);
        if (blacklistsByCitizenId.isPresent()) {
            return blacklistsByCitizenId.get().getId();
        }
        final Citizen citizen = citizenRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });

        Blacklist blacklist = new Blacklist();
        blacklist.setCitizen(citizen);
        return blacklistRepository.save(blacklist).getId();
    }

    @Override
    public boolean isCitizenBlocked(Long id) {
        Optional<Blacklist> blacklist = blacklistRepository.findBlacklistsByCitizenId(id);
        return blacklist.isPresent();
    }

    @Override
    public Long deleteCitizenFromBlacklist(Long id) {
        final Blacklist blacklist = blacklistRepository.findBlacklistsByCitizenId(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.BLACKLIST_NOT_FOUND);
        });
        blacklistRepository.delete(blacklist);
        return blacklist.getId();
    }

    @Override
    public List<StatisticsDTO> getBasicStatistics() {
        final long totalCitizens = citizenRepository.countAllUserCitizens();
        final long totalBlockedCitizens = blacklistRepository.count();
        StatisticsDTO statisticsDTO1 = new StatisticsDTO();
        statisticsDTO1.setState("Total");
        statisticsDTO1.setVal((int) totalCitizens);
        StatisticsDTO statisticsDTO2 = new StatisticsDTO();
        statisticsDTO2.setState("Blocați");
        statisticsDTO2.setVal((int) totalBlockedCitizens);

        return List.of(statisticsDTO1, statisticsDTO2);
    }
}
