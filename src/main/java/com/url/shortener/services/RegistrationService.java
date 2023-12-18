package com.url.shortener.services;

import com.url.shortener.exceptions.users.*;
import com.url.shortener.models.User;
import com.url.shortener.payload.RegistrationRequest;
import com.url.shortener.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.url.shortener.utils.EmailValidator.isValidEmail;
import static com.url.shortener.utils.UsernameValidator.isValidUsername;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String register(RegistrationRequest request) {
        try {
            validateRegistrationRequest(request);
            // Save the user and send a verification email
            saveUser(request);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException | InvalidEmailException |
                 InvalidUsernameException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Registration failed with error: {}", ex.getMessage());
            throw new RegistrationException("Registration failed with error: " + ex.getMessage());
        }
        return "User registered successfully!";
    }

    private void validateRegistrationRequest(RegistrationRequest request) {
        validateUsername(request.getUsername());
        validateEmail(request.getEmail());
    }

    private void validateUsername(String username) {
        if (!isValidUsername(username)) throw new InvalidUsernameException("Username is not valid!");
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("The username is already taken");
        }
    }

    private void validateEmail(String email) {
        if (!isValidEmail(email)) throw new InvalidEmailException("Email is not valid!");
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("The email address is already taken");
        }
    }

    private void saveUser(RegistrationRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();

        // Save the user and return it
        userRepository.save(user);
    }
}
