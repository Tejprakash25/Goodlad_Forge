package com.goodlad.forge.leaderboard.controller;

import com.goodlad.forge.common.response.ApiResponse;
import com.goodlad.forge.leaderboard.service.LeaderboardEntry;
import com.goodlad.forge.leaderboard.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaderboardEntry>>> getGlobal() {
        return ResponseEntity.ok(ApiResponse.ok(leaderboardService.getGlobalLeaderboard()));
    }

    @GetMapping("/top/{n}")
    public ResponseEntity<ApiResponse<List<LeaderboardEntry>>> getTopN(@PathVariable int n) {
        if (n < 1 || n > 100) {
            return ResponseEntity.badRequest().body(ApiResponse.error("n must be between 1 and 100"));
        }
        return ResponseEntity.ok(ApiResponse.ok(leaderboardService.getTopN(n)));
    }
}
