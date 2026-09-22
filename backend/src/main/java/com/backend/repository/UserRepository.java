package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
