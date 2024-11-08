package com.example.timetracker.time_tracking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.example.timetracker.time_tracking.WorkSession.Status;

@Repository
public interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
    List<WorkSession> findByUserId(Long userId);

    // Найти активную сессию пользователя по статусу IN_PROGRESS
    Optional<WorkSession> findByUserIdAndStatus(Long userId, Status status);
}
