package com.sika.demo.base.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Hidden
    private Long userId;

    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
}

