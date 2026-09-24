package com.backend.service;

import java.time.Instant;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.AuthResponse;
import com.backend.dto.CreateUserRequest;
import com.backend.dto.RegisterRequest;
import com.backend.dto.UserResponse;
import com.backend.entity.User;
import com.backend.entity.UserRole;
import com.backend.entity.UserStatus;
import com.backend.exception.InactiveUserException;
import com.backend.exception.InvalidCredentialsException;
import com.backend.repository.UserRepository;
import com.backend.security.JwtService;

@Service
@Transactional(readOnly = true)
public class AuthService {
    /** Hash descartável usado para igualar o tempo de resposta quando o e-mail não existe. */
    private static final String DUMMY_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(normalizeEmail(email)).orElse(null);
        if (user == null) {
            passwordEncoder.matches(password, DUMMY_HASH);
            throw new InvalidCredentialsException();
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InactiveUserException();
        }

        user.setLastAccess(Instant.now());
        userRepository.saveAndFlush(user);
        return new AuthResponse(jwtService.generate(user), UserResponse.from(user));
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        return userService.create(new CreateUserRequest(request.name(), request.email(), request.phone(),
                request.password(), UserRole.OPERATOR));
    }

    public UserResponse currentUser(Long id) {
        return userService.findById(id);
    }

    private String normalizeEmail(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }
}
