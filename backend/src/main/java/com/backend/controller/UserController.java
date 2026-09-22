package com.backend.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.ApiResponse;
import com.backend.dto.CreateUserRequest;
import com.backend.dto.UpdateUserRequest;
import com.backend.dto.UpdateUserStatusRequest;
import com.backend.dto.UserPageResponse;
import com.backend.dto.UserResponse;
import com.backend.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserPageResponse findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "100") @Min(1) @Max(100) int limit) {
        return UserPageResponse.from(userService.findAll(page, limit));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable @Positive Long id) {
        return new ApiResponse<>(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = userService.create(request);
        return ResponseEntity.created(URI.create("/api/users/" + user.id()))
                .body(new ApiResponse<>(user));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable @Positive Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return new ApiResponse<>(userService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<UserResponse> updateStatus(@PathVariable @Positive Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        return new ApiResponse<>(userService.updateStatus(id, request.status()));
    }
}
