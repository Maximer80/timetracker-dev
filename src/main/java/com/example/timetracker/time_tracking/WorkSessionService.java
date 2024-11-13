package com.example.timetracker.time_tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class WorkSessionService {

    private final WorkSessionRepository workSessionRepository;

    @Autowired
    public WorkSessionService(WorkSessionRepository workSessionRepository) {
        this.workSessionRepository = workSessionRepository;
    }

    public Optional<WorkSession> getActiveSessionByUserId(Long userId) {
        return workSessionRepository.findByUserIdAndStatus(userId, WorkSession.Status.IN_PROGRESS);
    }

    public WorkSession startSession(Long userId) {
        // Ищем активную сессию (IN_PROGRESS) для пользователя
        Optional<WorkSession> activeSession = getActiveSessionByUserId(userId);

        if (activeSession.isPresent()) {
            WorkSession existingSession = activeSession.get();

            // Создаём форматтер для нужного формата даты и времени
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");

            // Форматируем дату и вставляем в сообщение
            String formattedStartTime = existingSession.getStartTime().format(formatter);

            throw new IllegalStateException("Рабочая сессия уже начата " + formattedStartTime);
        }

        // Создаём новую сессию, если активной нет
        WorkSession session = new WorkSession(userId, LocalDateTime.now());
        return workSessionRepository.save(session);
    }

    public Optional<WorkSession> endSession(Long sessionId) {
        Optional<WorkSession> optionalSession = workSessionRepository.findById(sessionId);
        if (optionalSession.isPresent()) {
            WorkSession session = optionalSession.get();
            session.setEndTime(LocalDateTime.now());
            session.setStatus(WorkSession.Status.COMPLETED); // Обновляем статус на COMPLETED
            workSessionRepository.save(session);
        }
        return optionalSession;
    }

    public List<WorkSession> getAllSessions() {
        return workSessionRepository.findAll();
    }

    public List<WorkSession> getSessionsByUserId(Long userId) {
        return workSessionRepository.findByUserId(userId);
    }

    public Optional<WorkSession> getSessionById(Long sessionId) {
        return workSessionRepository.findById(sessionId);
    }
}
