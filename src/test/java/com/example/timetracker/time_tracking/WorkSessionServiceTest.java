package com.example.timetracker.time_tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkSessionServiceTest {

    @Mock
    private WorkSessionRepository workSessionRepository;

    @InjectMocks
    private WorkSessionService workSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация Mockito
    }
    
    @Test
    void startSession_ShouldCreateAndSaveWorkSession() {
        Long userId = 1L;
        WorkSession mockSession = new WorkSession(userId, LocalDateTime.now());

        when(workSessionRepository.save(any(WorkSession.class))).thenReturn(mockSession);

        // Запускаем метод и проверяем результат
        WorkSession result = workSessionService.startSession(userId);
        
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getStartTime());
        verify(workSessionRepository, times(1)).save(any(WorkSession.class));
    }
    
    @Test
    void endSession_ShouldEndWorkSessionIfExists() {
        Long sessionId = 1L;
        WorkSession mockSession = new WorkSession(1L, LocalDateTime.now());
        
        when(workSessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(workSessionRepository.save(mockSession)).thenReturn(mockSession);
        
        // Запускаем метод и проверяем результат
        Optional<WorkSession> result = workSessionService.endSession(sessionId);

        assertTrue(result.isPresent());
        assertNotNull(result.get().getEndTime());
        assertEquals(WorkSession.Status.COMPLETED, result.get().getStatus());
        verify(workSessionRepository, times(1)).save(mockSession);
    }

    @Test
    void endSession_ShouldReturnEmptyOptionalIfSessionNotFound() {
        Long sessionId = 1L;

        when(workSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Optional<WorkSession> result = workSessionService.endSession(sessionId);

        assertFalse(result.isPresent());
        verify(workSessionRepository, never()).save(any(WorkSession.class));
    }
    
    @Test
    void getAllSessions_ShouldReturnAllSessions() {
        List<WorkSession> mockSessions = List.of(
                new WorkSession(1L, LocalDateTime.now()),
                new WorkSession(2L, LocalDateTime.now())
        );

        when(workSessionRepository.findAll()).thenReturn(mockSessions);

        List<WorkSession> result = workSessionService.getAllSessions();

        assertEquals(2, result.size());
        verify(workSessionRepository, times(1)).findAll();
    }
    
    @Test
    void getSessionsByUserId_ShouldReturnSessionsForGivenUser() {
        Long userId = 1L;
        List<WorkSession> mockSessions = List.of(
                new WorkSession(userId, LocalDateTime.now())
        );

        when(workSessionRepository.findByUserId(userId)).thenReturn(mockSessions);

        List<WorkSession> result = workSessionService.getSessionsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        verify(workSessionRepository, times(1)).findByUserId(userId);
    }
    
    @Test
    void getSessionById_ShouldReturnSessionIfExists() {
        Long sessionId = 1L;
        WorkSession mockSession = new WorkSession(1L, LocalDateTime.now());

        when(workSessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        Optional<WorkSession> result = workSessionService.getSessionById(sessionId);

        assertTrue(result.isPresent());
        assertEquals(mockSession, result.get());
        verify(workSessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void getSessionById_ShouldReturnEmptyOptionalIfNotExists() {
        Long sessionId = 1L;

        when(workSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Optional<WorkSession> result = workSessionService.getSessionById(sessionId);

        assertFalse(result.isPresent());
        verify(workSessionRepository, times(1)).findById(sessionId);
    }
}
