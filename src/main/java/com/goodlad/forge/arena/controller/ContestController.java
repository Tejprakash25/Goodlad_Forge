package com.goodlad.forge.arena.controller;

import com.goodlad.forge.arena.dto.ContestRequest;
import com.goodlad.forge.arena.dto.ContestResponse;
import com.goodlad.forge.arena.service.ContestService;
import com.goodlad.forge.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arena/contests")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @PostMapping
    public ResponseEntity<ApiResponse<ContestResponse>> create(@Valid @RequestBody ContestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Contest created", contestService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContestResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(contestService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContestResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(contestService.getById(id)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ContestResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.ok(contestService.getActive()));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<ContestResponse>>> getUpcoming() {
        return ResponseEntity.ok(ApiResponse.ok(contestService.getUpcoming()));
    }
}
