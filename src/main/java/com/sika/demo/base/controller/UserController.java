package com.sika.demo.base.controller;

import com.sika.demo.base.entity.UserEntity;
import com.sika.demo.base.model.ApiResponseDTO;
import com.sika.demo.base.model.AuthRequest;
import com.sika.demo.base.model.AuthResponse;
import com.sika.demo.base.model.RegisterRequest;
import com.sika.demo.base.service.UserService;
import com.sika.demo.base.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserEntity create(@RequestBody UserEntity user) {
        return userService.createUser(user);
    }

    @PostMapping("/register")
    public ApiResponseDTO<Map<String, String>> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ApiResponseDTO<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        return ResponseBuilder.success(
                "USER_LOGIN_SUCCESS",
                "User logged in successfully",
                userService.login(request, response)
        );
    }

    @GetMapping
    public List<UserEntity> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public List<UserEntity> search(@RequestParam String name) {
        return userService.searchUsers(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == #user.userId and #id == authentication.principal.id")
    public ApiResponseDTO<Map<String, String>> update(@PathVariable Long id, @RequestBody UserEntity user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id and #id == #user.userId")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

