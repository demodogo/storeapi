package com.duoc.storeapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.storeapi.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
