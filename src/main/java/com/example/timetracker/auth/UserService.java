package com.example.timetracker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // Кодируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Сохраняем пользователя в базе данных
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Новый метод для аутентификации пользователя
    public boolean authenticateUser(String username, String password) {
        User user = findByUsername(username);
        if (user != null) {
            // Сравниваем пароль
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false; // Пользователь не найден
    }
}
