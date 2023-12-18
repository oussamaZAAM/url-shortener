package com.url.shortener.controllers;

import com.url.shortener.exceptions.users.EmailAlreadyExistsException;
import com.url.shortener.exceptions.users.InvalidEmailException;
import com.url.shortener.exceptions.users.InvalidUsernameException;
import com.url.shortener.exceptions.users.UsernameAlreadyExistsException;
import com.url.shortener.payload.AuthenticationRequest;
import com.url.shortener.payload.AuthenticationResponse;
import com.url.shortener.payload.RegistrationRequest;
import com.url.shortener.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class AuthController {

    private final RegistrationService registrationService;

     public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
     }
//    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            log.info("Received registration request for username {}", request.getUsername());
            String message = registrationService.register(request);
            log.info("User {} registered successfully", request.getUsername());
            return ResponseEntity.ok(message);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        } catch (InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
//        log.info("Received authentication request for username {}", request.getUsername());
//        AuthenticationResponse response = authenticationService.authenticate(request);
//        log.info("User {} authenticated successfully", request.getUsername());
//        return ResponseEntity.ok(response);
//    }
}
