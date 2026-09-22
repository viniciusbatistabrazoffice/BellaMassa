package com.backend.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.CreateUserRequest;
import com.backend.dto.UpdateUserRequest;
import com.backend.dto.UserResponse;
import com.backend.entity.User;
import com.backend.entity.UserStatus;
import com.backend.exception.DuplicateEmailException;
import com.backend.exception.UserNotFoundException;
import com.backend.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserResponse> findAll(int page, int limit) {
        return userRepository.findAll(PageRequest.of(page, limit, Sort.by("id")))
                .map(UserResponse::from);
    }

    public UserResponse findById(Long id) {
        return UserResponse.from(findUser(id));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException();
        }

        User user = new User(request.name().strip(), email);
        user.setPhone(normalizePhone(request.phone()));
        user.setRole(request.role());
        user.setPasswordHash(encodePassword(request.password()));
        return UserResponse.from(userRepository.saveAndFlush(user));
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = findUser(id);
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateEmailException();
        }

        user.setUsername(request.name().strip());
        user.setEmail(email);
        user.setPhone(normalizePhone(request.phone()));
        user.setRole(request.role());
        if (request.password() != null && !request.password().isEmpty()) {
            user.setPasswordHash(encodePassword(request.password()));
        }
        return UserResponse.from(userRepository.saveAndFlush(user));
    }

    @Transactional
    public UserResponse updateStatus(Long id, UserStatus status) {
        User user = findUser(id);
        user.setStatus(status);
        return UserResponse.from(userRepository.saveAndFlush(user));
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private String normalizeEmail(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }

    private String normalizePhone(String phone) {
        return phone == null || phone.isBlank() ? null : phone.strip();
    }

    private String encodePassword(String password) {
        if (password == null || password.isBlank() || password.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("A senha deve ocupar no máximo 72 bytes em UTF-8.");
        }
        return passwordEncoder.encode(password);
    }
}
