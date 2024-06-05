package com.surgee.ScandaPay.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surgee.ScandaPay.model.ConfirmationToken;


@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
    Optional<ConfirmationToken> findByUserId(Long id);
}
