package com.busreservation.backend.service;

import com.busreservation.backend.dto.UserLoginRequest;
import com.busreservation.backend.dto.UserRegisterRequest;
import com.busreservation.backend.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest request);
    UserResponse loginUser(UserLoginRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
}
