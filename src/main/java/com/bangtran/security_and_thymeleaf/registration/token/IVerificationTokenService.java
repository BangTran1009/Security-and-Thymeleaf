package com.bangtran.security_and_thymeleaf.registration.token;

import com.bangtran.security_and_thymeleaf.user.User;

import java.util.Optional;

public interface IVerificationTokenService {
    String validateToken(String token);
    void saveVerificationTokenForUser(User user, String token);
    Optional<VerificationToken> findByToken(String token);
}
