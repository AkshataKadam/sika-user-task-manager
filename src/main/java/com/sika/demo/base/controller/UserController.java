package com.sika.demo.base.controller;

import com.sika.demo.base.entity.UserEntity;
import com.sika.demo.base.model.ApiResponseDTO;
import com.sika.demo.base.model.AuthRequest;
import com.sika.demo.base.model.AuthResponse;
import com.sika.demo.base.model.RegisterRequest;
import com.sika.demo.base.service.UserService;
import com.sika.demo.base.service.impl.TaskServiceImpl;
import com.sika.demo.base.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    protected final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping("/create")
    public ApiResponseDTO<Map<String, String>> createUser(@RequestBody RegisterRequest request) {
        LOGGER.info("Executing UserController: createUser()");
        return userService.register(request);
    }

    @PostMapping("/login")
    public ApiResponseDTO<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        LOGGER.info("Executing UserController: login()");
        return ResponseBuilder.success(
                "USER_LOGIN_SUCCESS",
                "User logged in successfully",
                userService.login(request, response)
        );
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {
        LOGGER.info("Executing UserController: getAllUsers()");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        LOGGER.info("Executing UserController: getUserById()");
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public List<UserEntity> searchUserByName(@RequestParam String name) {
        LOGGER.info("Executing UserController: searchUserByName()");
        return userService.searchUsers(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ApiResponseDTO<Map<String, String>> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        LOGGER.info("Executing UserController: updateUser()");
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public void deleteUser(@PathVariable Long id) {
        LOGGER.info("Executing UserController: deleteUser()");
        userService.deleteUser(id);
    }
}

