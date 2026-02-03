package com.sika.demo.base.service;

import com.sika.demo.base.entity.UserEntity;
import com.sika.demo.base.model.ApiResponseDTO;
import com.sika.demo.base.model.AuthRequest;
import com.sika.demo.base.model.AuthResponse;
import com.sika.demo.base.model.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserEntity createUser(UserEntity user);

    AuthResponse login(AuthRequest req, HttpServletResponse res);

    UserEntity getUserById(Long id);

    ApiResponseDTO<Map<String, String>> updateUser(Long id, UserEntity updatedUser);

    void deleteUser(Long id);

    List<UserEntity> getAllUsers();

    List<UserEntity> searchUsers(String name);

    ApiResponseDTO<Map<String, String>> register(RegisterRequest request);
}

