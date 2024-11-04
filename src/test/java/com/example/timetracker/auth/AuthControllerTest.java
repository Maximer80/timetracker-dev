/*
package com.example.timetracker.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testLoginPublicRouteSuccess() throws Exception {
        Mockito.when(userService.authenticateUser("timetracker_user", "user2024")).thenReturn(true);

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("newuser1");
        loginRequest.setPassword("pass123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    public void testLoginPublicRouteFailure() throws Exception {
        Mockito.when(userService.authenticateUser("invalidUser", "wrongPassword")).thenReturn(false);

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("invalidUser");
        loginRequest.setPassword("wrongPassword");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Неверные учётные данные пользователя"));
    }
}
*/