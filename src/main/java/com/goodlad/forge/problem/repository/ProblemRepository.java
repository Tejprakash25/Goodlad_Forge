package com.goodlad.forge.problem.repository;

import com.goodlad.forge.problem.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByActiveTrue();

    List<Problem> findByDifficultyAndActiveTrue(Problem.Difficulty difficulty);
}
