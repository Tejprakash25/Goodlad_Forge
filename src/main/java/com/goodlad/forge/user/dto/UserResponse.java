package com.goodlad.forge.user.dto;

import com.goodlad.forge.user.model.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final int problemsSolved;
    private final int totalScore;
    private final LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.problemsSolved = user.getProblemsSolved();
        this.totalScore = user.getTotalScore();
        this.createdAt = user.getCreatedAt();
    }
}
