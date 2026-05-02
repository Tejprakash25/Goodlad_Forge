package com.goodlad.forge.leaderboard.service;

import com.goodlad.forge.user.model.User;
import com.goodlad.forge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final UserRepository userRepository;

    /**
     * Global leaderboard — sorted by totalScore DESC, then problemsSolved DESC.
     */
    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getGlobalLeaderboard() {
        List<User> users = userRepository.findAll();

        AtomicInteger rankCounter = new AtomicInteger(1);

        return users.stream()
                .filter(u -> u.getProblemsSolved() > 0)
                .sorted(Comparator.comparingInt(User::getTotalScore).reversed()
                        .thenComparingInt(User::getProblemsSolved).reversed())
                .map(u -> new LeaderboardEntry(
                        rankCounter.getAndIncrement(),
                        u.getId(),
                        u.getUsername(),
                        u.getProblemsSolved(),
                        u.getTotalScore()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Top N users — for homepage widgets or arena banners.
     */
    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getTopN(int n) {
        return getGlobalLeaderboard().stream()
                .limit(n)
                .collect(Collectors.toList());
    }
}
