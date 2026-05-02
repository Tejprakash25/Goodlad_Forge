package com.goodlad.forge.arena.dto;

import com.goodlad.forge.arena.model.Contest;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ContestResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String status;
    private final List<Long> problemIds;
    private final LocalDateTime createdAt;

    public ContestResponse(Contest contest) {
        this.id = contest.getId();
        this.title = contest.getTitle();
        this.description = contest.getDescription();
        this.startTime = contest.getStartTime();
        this.endTime = contest.getEndTime();
        this.status = contest.getStatus().name();
        this.problemIds = contest.getProblemIds();
        this.createdAt = contest.getCreatedAt();
    }
}
