package com.sika.demo.base.repository;

import com.sika.demo.base.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName
    );

    Optional<UserEntity> findByEmail(String email);
}

