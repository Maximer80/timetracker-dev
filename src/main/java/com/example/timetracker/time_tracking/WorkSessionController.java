package com.example.timetracker.time_tracking;

import com.example.timetracker.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/work-sessions")
public class WorkSessionController {

    private final WorkSessionService workSessionService;
    private final UserService userService;

    @Autowired
    public WorkSessionController(WorkSessionService workSessionService, UserService userService) {
        this.workSessionService = workSessionService;
        this.userService = userService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        try {
            // Попытка создать рабочую сессию
            WorkSession session = workSessionService.startSession(userId);
            return ResponseEntity.ok(session);
        } catch (IllegalStateException e) {
            // Если сессия уже активна, отобразим ошибку
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<WorkSession> endSession(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        Optional<WorkSession> session = workSessionService.getSessionById(id);

        if (session.isPresent() && (session.get().getUserId().equals(userId) || isAdmin(authentication))) {
            WorkSession endedSession = workSessionService.endSession(id).orElseThrow();
            return ResponseEntity.ok(endedSession);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<WorkSession>> getAllSessions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<WorkSession> sessions;

        if (isAdmin(authentication)) {
            sessions = workSessionService.getAllSessions();
        } else {
            String currentUsername = authentication.getName();
            Long userId = userService.getUserIdByUsername(currentUsername);
            sessions = workSessionService.getSessionsByUserId(userId);
        }

        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkSession> getSessionById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        Optional<WorkSession> session = workSessionService.getSessionById(id);
        if (session.isPresent() && (session.get().getUserId().equals(userId) || isAdmin(authentication))) {
            return ResponseEntity.ok(session.get());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ADMIN"));
    }
}
