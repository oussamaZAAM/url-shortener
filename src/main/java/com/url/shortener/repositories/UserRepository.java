package com.url.shortener.repositories;

import com.url.shortener.models.UrlEntity;
import com.url.shortener.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, @Email @NotNull String email);
    Optional<User> findByEmail(@Email @NotNull String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
