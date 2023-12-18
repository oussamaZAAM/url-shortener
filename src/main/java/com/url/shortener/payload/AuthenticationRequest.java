package com.url.shortener.payload;

import com.url.shortener.utils.ValidAuthenticationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidAuthenticationRequest(message = "The authentication request is not valid.")
public class AuthenticationRequest {
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    private String password;
}
