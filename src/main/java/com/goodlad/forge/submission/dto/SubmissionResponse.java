package com.goodlad.forge.submission.dto;

import com.goodlad.forge.submission.model.Submission;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SubmissionResponse {

    private final Long id;
    private final Long userId;
    private final String username;
    private final Long problemId;
    private final String problemTitle;
    private final String language;
    private final String status;
    private final Long timeTakenSeconds;
    private final int score;
    private final boolean duplicate;
    private final Long contestId;
    private final LocalDateTime submittedAt;

    public SubmissionResponse(Submission submission) {
        this.id = submission.getId();
        this.userId = submission.getUser().getId();
        this.username = submission.getUser().getUsername();
        this.problemId = submission.getProblem().getId();
        this.problemTitle = submission.getProblem().getTitle();
        this.language = submission.getLanguage();
        this.status = submission.getStatus().name();
        this.timeTakenSeconds = submission.getTimeTakenSeconds();
        this.score = submission.getScore();
        this.duplicate = submission.isDuplicate();
        this.contestId = submission.getContestId();
        this.submittedAt = submission.getSubmittedAt();
    }
}
