package com.busreservation.backend.service.impl;

import com.busreservation.backend.dto.UserLoginRequest;
import com.busreservation.backend.dto.UserRegisterRequest;
import com.busreservation.backend.dto.UserResponse;
import com.busreservation.backend.entity.Role;
import com.busreservation.backend.entity.User;
import com.busreservation.backend.exception.BadRequestException;
import com.busreservation.backend.exception.ResourceNotFoundException;
import com.busreservation.backend.repository.UserRepository;
import com.busreservation.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().trim().toLowerCase())
                .password(request.getPassword()) // In production, hash with BCryptPasswordEncoder
                .phone(request.getPhone())
                .role(request.getRole() != null ? request.getRole() : Role.ROLE_USER)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
