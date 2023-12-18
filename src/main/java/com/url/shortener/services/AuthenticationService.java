//package com.url.shortener.services;
//
//import com.url.shortener.exceptions.users.AccountNotEnabledException;
//import com.url.shortener.exceptions.users.AuthenticationFailedException;
//import com.url.shortener.models.User;
//import com.url.shortener.payload.AuthenticationRequest;
//import com.url.shortener.payload.AuthenticationResponse;
//import com.url.shortener.repositories.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final JwtEncoder jwtEncoder;
//
//    private final static Integer REFRESH_TOKEN_EXPIRE_DATE_IN_DAYS = 7;
//    private final static Integer ACCESS_TOKEN_EXPIRE_DATE_IN_MINUTES = 15;
//
//    @Transactional
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//            String subject = authentication.getName();
//            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
//            log.info("Password grant authentication successful for user: {}", subject);
//            return buildAuthenticationResponse(subject, scope);
//        } catch (BadCredentialsException e) {
//            throw new AuthenticationFailedException("Username or password Incorrect");
//        }
//        catch (DisabledException e) {
//            User user = userRepository.findByUsernameOrEmail(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//            throw new AccountNotEnabledException("Account with username: " + user.getUsername() + " is not enabled!");
//        }
//    }
//
//    private AuthenticationResponse buildAuthenticationResponse(String subject, String scope) {
//        Instant instant = Instant.now();
//        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder().subject(subject).issuedAt(instant).expiresAt(instant.plus(ACCESS_TOKEN_EXPIRE_DATE_IN_MINUTES, ChronoUnit.MINUTES)).issuer("url-shortener").claim("scope", scope).build();
//        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
//
//        log.info("Authentication response generated for user: {}", subject);
//        return AuthenticationResponse.builder().accessToken(jwtAccessToken).build();
//    }
//}
