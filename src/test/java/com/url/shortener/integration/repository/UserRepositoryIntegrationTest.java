package com.url.shortener.integration.repository;

import com.url.shortener.integration.config.TestMongoConfig;
import com.url.shortener.models.User;
import com.url.shortener.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(TestMongoConfig.class)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testExistsByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("existingUser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByUsername("existingUser");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUsername_NotExists() {
        // Act
        boolean exists = userRepository.existsByUsername("nonExistingUser");

        // Assert
        assertThat(exists).isFalse();
    }

}
