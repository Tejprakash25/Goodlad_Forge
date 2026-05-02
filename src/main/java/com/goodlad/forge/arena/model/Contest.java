package com.goodlad.forge.arena.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ContestStatus status = ContestStatus.UPCOMING;

    // Problem IDs included in this contest (stored as comma-separated for MVP simplicity)
    @ElementCollection
    @CollectionTable(name = "contest_problems", joinColumns = @JoinColumn(name = "contest_id"))
    @Column(name = "problem_id")
    @Builder.Default
    private List<Long> problemIds = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum ContestStatus {
        UPCOMING, ACTIVE, ENDED
    }

    // Helper: check if the contest is currently active based on time
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }
}
