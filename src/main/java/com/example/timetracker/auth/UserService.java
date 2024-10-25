package com.example.timetracker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@SuppressWarnings("unused")
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
        // Проверка, существует ли пользователь
    	Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
    	if (existingUser.isPresent()) {
    		throw new RuntimeException("Пользователь с таким именем уже существует");
    	}
    	// Кодируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Сохраняем пользователя в базе данных
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Новый метод для аутентификации пользователя
    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = findByUsername(username);
        return userOptional.map(user -> passwordEncoder.matches(password, user.getPassword())).orElse(false);
    }
}
