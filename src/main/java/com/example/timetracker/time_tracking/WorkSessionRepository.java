package com.example.timetracker.time_tracking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
    List<WorkSession> findByUserId(Long userId);
    // Здесь можно добавить дополнительные методы для поиска сессий по различным критериям, если нужно
}




