package com.goodlad.forge.problem.dto;

import com.goodlad.forge.problem.model.Problem;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProblemResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String difficulty;
    private final int points;
    private final int timeLimitSeconds;
    private final boolean active;
    private final LocalDateTime createdAt;

    public ProblemResponse(Problem problem) {
        this.id = problem.getId();
        this.title = problem.getTitle();
        this.description = problem.getDescription();
        this.difficulty = problem.getDifficulty().name();
        this.points = problem.getPoints();
        this.timeLimitSeconds = problem.getTimeLimitSeconds();
        this.active = problem.isActive();
        this.createdAt = problem.getCreatedAt();
    }
}
