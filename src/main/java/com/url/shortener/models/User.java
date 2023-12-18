package com.url.shortener.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails{

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * Email address of the user.
     */
    @Email
    @NotNull
    private String email;

    /**
     * Hashed password of the user.
     */
    @Size(min = 8)
    private String password;

    /**
     * Username of the user.
     */
    @Size(min = 1, max = 50)
    private String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    // Custom constructor
    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Builder class
    public static class Builder {
        private String username;
        private String email;
        private String password;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(username, email, password);
        }
    }

    // Static method to obtain a builder instance
    public static Builder builder() {
        return new Builder();
    }
}