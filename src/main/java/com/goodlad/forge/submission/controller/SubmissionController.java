package com.goodlad.forge.submission.controller;

import com.goodlad.forge.common.response.ApiResponse;
import com.goodlad.forge.submission.dto.SubmitRequest;
import com.goodlad.forge.submission.dto.SubmissionResponse;
import com.goodlad.forge.submission.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> submit(@Valid @RequestBody SubmitRequest request) {
        SubmissionResponse response = submissionService.submit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Submission received", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(submissionService.getById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(submissionService.getByUser(userId)));
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getByProblem(@PathVariable Long problemId) {
        return ResponseEntity.ok(ApiResponse.ok(submissionService.getByProblem(problemId)));
    }
}
