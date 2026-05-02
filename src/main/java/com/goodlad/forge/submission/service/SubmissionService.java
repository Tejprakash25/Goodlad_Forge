package com.goodlad.forge.submission.service;

import com.goodlad.forge.common.exception.ForgeException;
import com.goodlad.forge.problem.model.Problem;
import com.goodlad.forge.problem.service.ProblemService;
import com.goodlad.forge.submission.dto.SubmitRequest;
import com.goodlad.forge.submission.dto.SubmissionResponse;
import com.goodlad.forge.submission.model.Submission;
import com.goodlad.forge.submission.repository.SubmissionRepository;
import com.goodlad.forge.user.model.User;
import com.goodlad.forge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ProblemService problemService;

    @Transactional
    public SubmissionResponse submit(SubmitRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> ForgeException.notFound("User not found"));

        Problem problem = problemService.findEntityById(request.getProblemId());

        // ── Anti-cheat check 1: Duplicate code detection ──────────────────────
        boolean isDuplicate = submissionRepository.existsByUserIdAndProblemIdAndCode(
                request.getUserId(), request.getProblemId(), request.getCode());

        // ── Anti-cheat check 2: Time-limit enforcement ────────────────────────
        // If user claims they solved it faster than the minimum plausible time (10s), flag it
        boolean timeSuspicious = request.getTimeTakenSeconds() != null
                && request.getTimeTakenSeconds() < 10;

        // ── Evaluate submission (MVP placeholder logic) ───────────────────────
        // In production: call a code execution sandbox here.
        // For MVP: any submission with code length > 10 chars is ACCEPTED.
        Submission.Status status;
        int score = 0;

        if (isDuplicate) {
            status = Submission.Status.WRONG_ANSWER; // penalise exact duplicates
        } else if (timeSuspicious) {
            status = Submission.Status.WRONG_ANSWER; // flag suspicious speed
        } else if (request.getCode().trim().length() > 10) {
            status = Submission.Status.ACCEPTED;
            score = calculateScore(problem, request.getTimeTakenSeconds());
        } else {
            status = Submission.Status.WRONG_ANSWER;
        }

        Submission submission = Submission.builder()
                .user(user)
                .problem(problem)
                .code(request.getCode())
                .language(request.getLanguage())
                .status(status)
                .timeTakenSeconds(request.getTimeTakenSeconds())
                .score(score)
                .duplicate(isDuplicate)
                .contestId(request.getContestId())
                .build();

        Submission saved = submissionRepository.save(submission);

        // ── Update user stats on accepted submissions ─────────────────────────
        if (status == Submission.Status.ACCEPTED && !isDuplicate) {
            boolean alreadySolvedBefore = submissionRepository
                    .findByUserIdAndProblemIdAndStatus(user.getId(), problem.getId(), Submission.Status.ACCEPTED)
                    .isPresent();

            // Only count as a new solve if this is their first ACCEPTED for this problem
            // (the current save is ACCEPTED, so we check count > 1)
            long acceptedCount = submissionRepository.findByUserId(user.getId()).stream()
                    .filter(s -> s.getStatus() == Submission.Status.ACCEPTED
                            && s.getProblem().getId().equals(problem.getId()))
                    .count();

            if (acceptedCount <= 1) {
                user.setProblemsSolved(user.getProblemsSolved() + 1);
            }
            user.setTotalScore(user.getTotalScore() + score);
            userRepository.save(user);
        }

        return new SubmissionResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getByUser(Long userId) {
        return submissionRepository.findByUserId(userId).stream()
                .map(SubmissionResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getByProblem(Long problemId) {
        return submissionRepository.findByProblemId(problemId).stream()
                .map(SubmissionResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getById(Long id) {
        return submissionRepository.findById(id)
                .map(SubmissionResponse::new)
                .orElseThrow(() -> ForgeException.notFound("Submission not found"));
    }

    /**
     * Score formula:
     * - Base score = problem.points
     * - Time bonus: if solved in less than half the allowed time, +50% bonus
     * - Time penalty: if solved after 80% of allowed time, -25% penalty
     */
    private int calculateScore(Problem problem, Long timeTakenSeconds) {
        int base = problem.getPoints();
        if (timeTakenSeconds == null) return base;

        double ratio = (double) timeTakenSeconds / problem.getTimeLimitSeconds();

        if (ratio < 0.5) {
            return (int) (base * 1.5);
        } else if (ratio > 0.8) {
            return (int) (base * 0.75);
        }
        return base;
    }
}
