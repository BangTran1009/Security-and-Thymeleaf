package com.bangtran.security_and_thymeleaf.user;

import com.bangtran.security_and_thymeleaf.registration.RegistrationRequest;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    User RegisterUser(RegistrationRequest registrationRequest);
    User findByEmail(String email);
}
