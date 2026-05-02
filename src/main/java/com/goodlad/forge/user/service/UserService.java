package com.goodlad.forge.user.service;

import com.goodlad.forge.common.exception.ForgeException;
import com.goodlad.forge.user.dto.LoginRequest;
import com.goodlad.forge.user.dto.RegisterRequest;
import com.goodlad.forge.user.dto.UserResponse;
import com.goodlad.forge.user.model.User;
import com.goodlad.forge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw ForgeException.conflict("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw ForgeException.conflict("Email '" + request.getEmail() + "' is already registered");
        }

        // NOTE: In production, hash the password (e.g. BCrypt). Skipped here for MVP simplicity.
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(User.Role.USER)
                .build();

        return new UserResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> ForgeException.notFound("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw ForgeException.badRequest("Invalid credentials");
        }

        return new UserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ForgeException.notFound("User not found with id: " + id));
        return new UserResponse(user);
    }
}
