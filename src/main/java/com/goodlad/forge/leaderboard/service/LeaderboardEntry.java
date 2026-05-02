package com.goodlad.forge.leaderboard.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardEntry {
    private final int rank;
    private final Long userId;
    private final String username;
    private final int problemsSolved;
    private final int totalScore;
}
