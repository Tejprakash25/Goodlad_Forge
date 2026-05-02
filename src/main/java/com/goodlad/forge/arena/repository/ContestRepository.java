package com.goodlad.forge.arena.repository;

import com.goodlad.forge.arena.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {

    List<Contest> findByStatus(Contest.ContestStatus status);
}
