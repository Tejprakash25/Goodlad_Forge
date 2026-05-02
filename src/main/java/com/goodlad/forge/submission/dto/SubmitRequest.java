package com.goodlad.forge.submission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Problem ID is required")
    private Long problemId;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Language is required")
    private String language;

    // Optional: time the user took to solve in seconds
    private Long timeTakenSeconds;

    // Optional: contest context
    private Long contestId;
}
