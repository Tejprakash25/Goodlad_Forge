package com.goodlad.forge.problem.dto;

import com.goodlad.forge.problem.model.Problem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Difficulty is required")
    private Problem.Difficulty difficulty;

    @Min(value = 1, message = "Points must be at least 1")
    private int points = 10;

    @Min(value = 30, message = "Time limit must be at least 30 seconds")
    private int timeLimitSeconds = 300;
}
