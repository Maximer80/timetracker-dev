package com.example.timetracker.time_tracking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
    // Здесь можно добавить дополнительные методы для поиска сессий по различным критериям, если нужно
}
