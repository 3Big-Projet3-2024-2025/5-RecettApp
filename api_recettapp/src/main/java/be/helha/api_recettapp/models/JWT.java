package be.helha.api_recettapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Diesbecq Aaron
 * Represents a token JWT.
 * This class uses Lombok annotation {@code @Data}, to automatically generate all getter and setter methods for each attribute.
 * It uses Lombok annotation {@code @AllArgsConstructor} to automatically generate a constructor with all attributes.
 */
@Data
@AllArgsConstructor
public class JWT {

    /**
     * Token used to grant access to protected resources
     */
    private String accessToken;

    /**
     * Token used to obtain a new access token after the current one expires,
     * extending the session without requiring re-authentication.
     */
    private String refreshToken;
}
