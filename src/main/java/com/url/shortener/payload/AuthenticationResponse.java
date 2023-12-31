package com.url.shortener.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token") // Specifies the name of the JSON property
    private String accessToken;
    /**
     * The JWT refresh token used for refreshing the access token.
     */
    @JsonProperty("refresh_token") // Specifies the name of the JSON property
    private String refreshToken;
}
