package com.surgee.ScandaPay.DAO;

import java.util.Optional;

import com.surgee.ScandaPay.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);
}
