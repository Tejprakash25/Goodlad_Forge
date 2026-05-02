package com.goodlad.forge.arena.service;

import com.goodlad.forge.arena.dto.ContestRequest;
import com.goodlad.forge.arena.dto.ContestResponse;
import com.goodlad.forge.arena.model.Contest;
import com.goodlad.forge.arena.repository.ContestRepository;
import com.goodlad.forge.common.exception.ForgeException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;

    @Transactional
    public ContestResponse create(ContestRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw ForgeException.badRequest("End time must be after start time");
        }

        Contest contest = Contest.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .problemIds(request.getProblemIds() != null ? request.getProblemIds() : List.of())
                .build();

        return new ContestResponse(contestRepository.save(contest));
    }

    @Transactional(readOnly = true)
    public List<ContestResponse> getAll() {
        return contestRepository.findAll().stream()
                .map(ContestResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContestResponse getById(Long id) {
        Contest contest = contestRepository.findById(id)
                .orElseThrow(() -> ForgeException.notFound("Contest not found with id: " + id));
        return new ContestResponse(contest);
    }

    @Transactional(readOnly = true)
    public List<ContestResponse> getActive() {
        return contestRepository.findByStatus(Contest.ContestStatus.ACTIVE).stream()
                .map(ContestResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContestResponse> getUpcoming() {
        return contestRepository.findByStatus(Contest.ContestStatus.UPCOMING).stream()
                .map(ContestResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Runs every 60 seconds to auto-transition contest statuses
     * based on the current time. No manual status updates needed.
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void syncContestStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Contest> all = contestRepository.findAll();

        for (Contest contest : all) {
            Contest.ContestStatus newStatus;

            if (now.isBefore(contest.getStartTime())) {
                newStatus = Contest.ContestStatus.UPCOMING;
            } else if (now.isAfter(contest.getEndTime())) {
                newStatus = Contest.ContestStatus.ENDED;
            } else {
                newStatus = Contest.ContestStatus.ACTIVE;
            }

            if (contest.getStatus() != newStatus) {
                contest.setStatus(newStatus);
                contestRepository.save(contest);
            }
        }
    }
}
