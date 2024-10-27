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
        WorkSession session = new WorkSession(userId, LocalDateTime.now(), "ACTIVE");
        return workSessionRepository.save(session);
    }

    public Optional<WorkSession> endSession(Long sessionId) {
        Optional<WorkSession> optionalSession = workSessionRepository.findById(sessionId);
        if (optionalSession.isPresent()) {
            WorkSession session = optionalSession.get();
            session.setEndTime(LocalDateTime.now());
            session.setStatus("COMPLETED");
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
