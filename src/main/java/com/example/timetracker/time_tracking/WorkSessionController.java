package com.example.timetracker.time_tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/work-sessions")
public class WorkSessionController {

    private final WorkSessionService workSessionService;

    @Autowired
    public WorkSessionController(WorkSessionService workSessionService) {
        this.workSessionService = workSessionService;
    }

    @PostMapping("/start")
    public ResponseEntity<WorkSession> startSession(@RequestParam Long userId) {
        WorkSession session = workSessionService.startSession(userId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<WorkSession> endSession(@PathVariable Long id) {
        Optional<WorkSession> session = workSessionService.endSession(id);
        return session.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<WorkSession>> getAllSessions() {
        List<WorkSession> sessions = workSessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkSession> getSessionById(@PathVariable Long id) {
        Optional<WorkSession> session = workSessionService.getSessionById(id);
        return session.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
