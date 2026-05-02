package com.goodlad.forge.problem.service;

import com.goodlad.forge.common.exception.ForgeException;
import com.goodlad.forge.problem.dto.ProblemRequest;
import com.goodlad.forge.problem.dto.ProblemResponse;
import com.goodlad.forge.problem.model.Problem;
import com.goodlad.forge.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional
    public ProblemResponse create(ProblemRequest request) {
        Problem problem = Problem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .difficulty(request.getDifficulty())
                .points(request.getPoints())
                .timeLimitSeconds(request.getTimeLimitSeconds())
                .build();

        return new ProblemResponse(problemRepository.save(problem));
    }

    @Transactional(readOnly = true)
    public List<ProblemResponse> getAll() {
        return problemRepository.findByActiveTrue().stream()
                .map(ProblemResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProblemResponse getById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> ForgeException.notFound("Problem not found with id: " + id));
        return new ProblemResponse(problem);
    }

    @Transactional(readOnly = true)
    public List<ProblemResponse> getByDifficulty(Problem.Difficulty difficulty) {
        return problemRepository.findByDifficultyAndActiveTrue(difficulty).stream()
                .map(ProblemResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivate(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> ForgeException.notFound("Problem not found with id: " + id));
        problem.setActive(false);
        problemRepository.save(problem);
    }

    // Used internally by other services to load the entity
    @Transactional(readOnly = true)
    public Problem findEntityById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> ForgeException.notFound("Problem not found with id: " + id));
    }
}
