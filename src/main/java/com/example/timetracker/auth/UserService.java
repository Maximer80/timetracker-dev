package com.example.timetracker.auth;

import com.example.timetracker.exception.UserAlreadyExistsException;
import com.example.timetracker.exception.InsufficientPermissionsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user, String currentUserRole) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }
        
        // Убедитесь, что только admin может регистрировать новых пользователей
        if (!"admin".equals(currentUserRole)) {
            throw new InsufficientPermissionsException("Недостаточно прав для регистрации пользователя");
        }

        // Кодируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Устанавливаем роль по умолчанию, если не указана
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user"); // Роль по умолчанию
        }
        
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

    // Новый метод для получения userId по имени пользователя
    public Long getUserIdByUsername(String username) {
        return findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Пользователь с именем " + username + " не найден"));
    }
}
