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
    private final UserService userService; // Сервис для работы с пользователями

    @Autowired
    public WorkSessionController(WorkSessionService workSessionService, UserService userService) {
        this.workSessionService = workSessionService;
        this.userService = userService;
    }

    @PostMapping("/start")
    public ResponseEntity<WorkSession> startSession() {
        // Получаем текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        // Запускаем сессию для текущего пользователя
        WorkSession session = workSessionService.startSession(userId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<WorkSession> endSession(@PathVariable Long id) {
        // Получаем текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        // Проверяем, принадлежит ли сессия текущему пользователю
        Optional<WorkSession> session = workSessionService.getSessionById(id);
        if (session.isPresent() && session.get().getUserId().equals(userId)) {
            WorkSession endedSession = workSessionService.endSession(id).orElseThrow();
            return ResponseEntity.ok(endedSession);
        } else {
            return ResponseEntity.status(403).build(); // Доступ запрещён
        }
    }

    @GetMapping
    public ResponseEntity<List<WorkSession>> getAllSessions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        // Получаем сессии только для текущего пользователя
        List<WorkSession> sessions = workSessionService.getSessionsByUserId(userId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkSession> getSessionById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        // Проверяем, принадлежит ли сессия текущему пользователю
        Optional<WorkSession> session = workSessionService.getSessionById(id);
        if (session.isPresent() && session.get().getUserId().equals(userId)) {
            return ResponseEntity.ok(session.get());
        } else {
            return ResponseEntity.status(403).build(); // Доступ запрещён
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkSession>> getSessionsByUserId(@PathVariable Long userId) {
        List<WorkSession> sessions = workSessionService.getSessionsByUserId(userId);
        return ResponseEntity.ok(sessions);
    }
}
