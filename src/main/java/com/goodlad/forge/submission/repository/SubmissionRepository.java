package com.goodlad.forge.submission.repository;

import com.goodlad.forge.submission.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByUserId(Long userId);

    List<Submission> findByProblemId(Long problemId);

    List<Submission> findByContestId(Long contestId);

    // Check if user already has an ACCEPTED submission for this problem
    Optional<Submission> findByUserIdAndProblemIdAndStatus(
            Long userId, Long problemId, Submission.Status status);

    // Anti-cheat: detect identical code submission for same problem by same user
    boolean existsByUserIdAndProblemIdAndCode(Long userId, Long problemId, String code);

    // Leaderboard: count accepted submissions per user
    @Query("SELECT s.user.id, COUNT(s) FROM Submission s WHERE s.status = 'ACCEPTED' GROUP BY s.user.id ORDER BY COUNT(s) DESC")
    List<Object[]> countAcceptedPerUser();
}
