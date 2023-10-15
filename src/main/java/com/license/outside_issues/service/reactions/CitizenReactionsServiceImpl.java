package com.license.outside_issues.service.reactions;

import com.license.outside_issues.dto.CitizenReactionsDTO;
import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.CitizenReactions;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.repository.CitizenReactionsRepository;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.repository.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitizenReactionsServiceImpl implements CitizenReactionsService {
    private final CitizenReactionsRepository citizenReactionsRepository;
    private final CitizenRepository citizenRepository;
    private final IssueRepository issueRepository;

    public CitizenReactionsServiceImpl(CitizenReactionsRepository citizenReactionsRepository, CitizenRepository citizenRepository, IssueRepository issueRepository) {
        this.citizenReactionsRepository = citizenReactionsRepository;
        this.citizenRepository = citizenRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    public int findByCitizenIdAndIssueId(Long citizenId, Long issueId) {
        if (citizenId == null && issueId == null) {
            return 0;
        }
        final Optional<CitizenReactions> byCitizenIdAndIssueId = citizenReactionsRepository.findByCitizenIdAndIssueId(citizenId, issueId);
        if (byCitizenIdAndIssueId.isPresent()) {
            boolean type = byCitizenIdAndIssueId.get().isType();
            return type ? 1 : -1;
        }
        return 0;
    }

    @Override
    public List<Long> addCitizenReaction(List<CitizenReactionsDTO> citizenReactions) {
        List<CitizenReactions> allReactions = citizenReactionsRepository.findAll();
        return citizenReactions.stream()
                .map(citizenReactionsDTO -> addOneReactionForCitizen(citizenReactionsDTO, allReactions))
                .collect(Collectors.toList());
    }

    private Long addOneReactionForCitizen(CitizenReactionsDTO citizenReaction, List<CitizenReactions> allReactions) {
        Citizen citizenById = citizenRepository.findById(citizenReaction.getCitizenId()).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        Issue issueById = issueRepository.findById(citizenReaction.getIssueId()).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        final Optional<CitizenReactions> currentCitizenReaction = allReactions.stream()
                .filter(reaction -> Objects.equals(reaction.getCitizen().getId(), citizenById.getId())
                        && Objects.equals(reaction.getIssue().getId(), issueById.getId()))
                .findFirst();

        if (currentCitizenReaction.isPresent()) {
            boolean type = citizenReaction.getType() == 1 ? Boolean.TRUE : Boolean.FALSE;
            Long id = currentCitizenReaction.get().getId();
            if (citizenReaction.getType() == 0) {
                citizenReactionsRepository.delete(currentCitizenReaction.get());
                issueById.setLikesNumber(issueById.getLikesNumber() - 1);
                issueRepository.save(issueById);
                return id;
            }
            else if (citizenReaction.getType() == -2) {
                citizenReactionsRepository.delete(currentCitizenReaction.get());
                issueById.setDislikesNumber(issueById.getDislikesNumber() - 1);
                issueRepository.save(issueById);
                return id;
            }
            else if (citizenReaction.getType() == 3) {
                citizenReactionsRepository.delete(currentCitizenReaction.get());
                issueById.setLikesNumber(issueById.getLikesNumber() + 1);
                issueRepository.save(issueById);
                return id;
            }
            else if (citizenReaction.getType() == 4) {
                citizenReactionsRepository.delete(currentCitizenReaction.get());
                issueById.setDislikesNumber(issueById.getDislikesNumber() + 1);
                issueRepository.save(issueById);
                return id;
            }
            else if ((citizenReaction.getType() == 1 && !currentCitizenReaction.get().isType())
                    || (citizenReaction.getType() == -1 && currentCitizenReaction.get().isType())) {
                if (type) {
                    issueById.setLikesNumber(issueById.getLikesNumber() + 1);
                    issueById.setDislikesNumber(issueById.getDislikesNumber() - 1);
                } else {
                    issueById.setLikesNumber(issueById.getLikesNumber() - 1);
                    issueById.setDislikesNumber(issueById.getDislikesNumber() + 1);
                }
                issueRepository.save(issueById);
                currentCitizenReaction.get().setType(type);
                CitizenReactions savedCitizenReactions = citizenReactionsRepository.save(currentCitizenReaction.get());
                return savedCitizenReactions.getId();
            }
            return currentCitizenReaction.get().getId();
        } else {
            if (citizenReaction.getType() != 0 && citizenReaction.getType() != -2) {
                final int type = citizenReaction.getType();
                CitizenReactions citizenReactions = new CitizenReactions();
                boolean currentType;
                if (type == 1 || type == 3) {
                    issueById.setLikesNumber(issueById.getLikesNumber() + 1);
                    currentType = true;
                } else {
                    issueById.setDislikesNumber(issueById.getDislikesNumber() + 1);
                    currentType = false;
                }
                Issue currentIssue = issueRepository.save(issueById);
                citizenReactions.setCitizen(citizenById);
                citizenReactions.setIssue(currentIssue);
                citizenReactions.setType(currentType);
                CitizenReactions savedCitizenReactions = citizenReactionsRepository.save(citizenReactions);
                return savedCitizenReactions.getId();
            }
            return -1L;
        }
    }
}
