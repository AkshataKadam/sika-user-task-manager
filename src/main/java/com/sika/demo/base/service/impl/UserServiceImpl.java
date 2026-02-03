package com.sika.demo.base.service.impl;

import com.sika.demo.base.entity.UserEntity;
import com.sika.demo.base.exception.ApiException;
import com.sika.demo.base.model.ApiResponseDTO;
import com.sika.demo.base.model.AuthRequest;
import com.sika.demo.base.model.AuthResponse;
import com.sika.demo.base.model.RegisterRequest;
import com.sika.demo.base.repository.UserRepository;
import com.sika.demo.base.service.UserService;
import com.sika.demo.base.utils.JwtUtil;
import com.sika.demo.base.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sika.demo.base.utils.PasswordUtil.hash;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    protected final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Override
    public UserEntity createUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public ApiResponseDTO<Map<String, String>> register(RegisterRequest request) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            LOGGER.error("Email already registered: {}", request.getEmail());
            return ResponseBuilder.error(
                    "EMAIL_ALREADY_REGISTERED",
                    request.getEmail() + " email already registered.");
        } else {
            UserEntity user = UserEntity.builder()
                    .email(request.getEmail())
                    .password(hash(request.getPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dateOfBirth(request.getDateOfBirth())
                    .build();
            userRepository.save(user);

            return ResponseBuilder.success(
                    "USER_REGISTRATION_SUCCESS",
                    "Registration successful.",
                    Map.of("email", user.getEmail())
            );
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserEntity> searchUsers(String name) {
        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    @Override
    public ApiResponseDTO<Map<String, String>> updateUser(Long id, UserEntity updatedUser) {
        if (!id.equals(updatedUser.getUserId())) {
            LOGGER.error("You cannot update this user as path ID and body userId must be the same");
            return ResponseBuilder.error(
                    "USER_UPDATE_FAILURE",
                    "You cannot update this user.");
        } else {
            UserEntity existing = getUserById(id);

            existing.setFirstName(updatedUser.getFirstName());
            existing.setLastName(updatedUser.getLastName());
            existing.setDateOfBirth(updatedUser.getDateOfBirth());
            userRepository.save(existing);

            return ResponseBuilder.success(
                    "USER_UPDATE_SUCCESS",
                    "User Updated successfully for " + existing.getEmail(),
                    Map.of("email", existing.getEmail())
            );
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    public AuthResponse login(AuthRequest req, HttpServletResponse res) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(req.getEmail());
        LOGGER.info(String.valueOf(existingUser));

        if (existingUser.isEmpty()) {
            LOGGER.error("User does not exist. Sign up to continue");
            throw  new ApiException("USER_NOT_FOUND", "User does not exist. Sign up to continue", 404);
        }  else {
            UserEntity entity = existingUser.get();
            UserDetails user = new org.springframework.security.core.userdetails.User(
                    entity.getEmail(),
                    entity.getPassword(),
                    Collections.emptyList()
            );
            LOGGER.info(String.valueOf(entity.getUserId()));

            String accessToken = jwtUtil.generateToken(user);

            LOGGER.info("accessToken: {}", accessToken);
            return new AuthResponse(accessToken);
        }
    }
}

