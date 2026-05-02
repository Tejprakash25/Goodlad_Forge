package com.goodlad.forge.submission.model;

import com.goodlad.forge.problem.model.Problem;
import com.goodlad.forge.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String language = "JAVA";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.PENDING;

    // Time taken to solve the problem in seconds (set on submission)
    private Long timeTakenSeconds;

    // Score awarded for this submission
    @Builder.Default
    private int score = 0;

    // Anti-cheat: track if this is a duplicate of a prior submission
    @Builder.Default
    private boolean duplicate = false;

    // If this submission was part of a contest
    @Column(name = "contest_id")
    private Long contestId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime submittedAt;

    public enum Status {
        PENDING, ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED, COMPILE_ERROR
    }
}
