package com.example.timetracker.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Base64;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_SuccessfulRegistration() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpassword");
        adminUser.setRole("admin");

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpassword");

        String authHeader = "Basic " + Base64.getEncoder().encodeToString("admin:adminpassword".getBytes());

        when(userService.authenticateUser("admin", "adminpassword")).thenReturn(true);
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userService.registerUser(newUser, "admin")).thenReturn(newUser);

        ResponseEntity<User> response = authController.register(newUser, authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newUser, response.getBody());
        verify(userService).registerUser(newUser, "admin");
    }

    @Test
    void register_Unauthorized() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpassword");

        String authHeader = "Basic " + Base64.getEncoder().encodeToString("user:wrongpassword".getBytes());

        when(userService.authenticateUser("user", "wrongpassword")).thenReturn(false);

        ResponseEntity<User> response = authController.register(newUser, authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(userService, never()).registerUser(any(User.class), anyString());
    }

    @Test
    void login_SuccessfulLogin() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        when(userService.authenticateUser("testuser", "testpassword")).thenReturn(true);

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    void login_Unauthorized() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        when(userService.authenticateUser("testuser", "wrongpassword")).thenReturn(false);

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Неверные учётные данные пользователя", response.getBody());
    }
}
