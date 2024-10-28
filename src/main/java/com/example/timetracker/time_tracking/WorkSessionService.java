package com.example.timetracker.time_tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkSessionService {

    private final WorkSessionRepository workSessionRepository;

    @Autowired
    public WorkSessionService(WorkSessionRepository workSessionRepository) {
        this.workSessionRepository = workSessionRepository;
    }

    public WorkSession startSession(Long userId) {
        // Теперь не передаем статус, так как он задается в конструкторе WorkSession
        WorkSession session = new WorkSession(userId, LocalDateTime.now());
        return workSessionRepository.save(session);
    }

    public Optional<WorkSession> endSession(Long sessionId) {
        Optional<WorkSession> optionalSession = workSessionRepository.findById(sessionId);
        if (optionalSession.isPresent()) {
            WorkSession session = optionalSession.get();
            session.setEndTime(LocalDateTime.now());
            // Устанавливаем статус с использованием enum Status
            session.setStatus(WorkSession.Status.COMPLETED);
            workSessionRepository.save(session);
        }
        return optionalSession;
    }

    public List<WorkSession> getAllSessions() {
        return workSessionRepository.findAll();
    }

    public Optional<WorkSession> getSessionById(Long sessionId) {
        return workSessionRepository.findById(sessionId);
    }
}
