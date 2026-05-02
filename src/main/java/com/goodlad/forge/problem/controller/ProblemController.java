package com.goodlad.forge.problem.controller;

import com.goodlad.forge.common.response.ApiResponse;
import com.goodlad.forge.problem.dto.ProblemRequest;
import com.goodlad.forge.problem.dto.ProblemResponse;
import com.goodlad.forge.problem.model.Problem;
import com.goodlad.forge.problem.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProblemResponse>> create(@Valid @RequestBody ProblemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Problem created", problemService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProblemResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(problemService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProblemResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(problemService.getById(id)));
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<ApiResponse<List<ProblemResponse>>> getByDifficulty(
            @PathVariable Problem.Difficulty difficulty) {
        return ResponseEntity.ok(ApiResponse.ok(problemService.getByDifficulty(difficulty)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        problemService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok("Problem deactivated", null));
    }
}
