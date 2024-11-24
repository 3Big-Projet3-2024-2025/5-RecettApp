package be.helha.api_recettapp.utils;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Diesbecq Aaron
 * Utility class for handling JSON Web Tokens (JWT).
 * This class uses annotation {@code @Component} to indicates that this class is a Spring component, eligible for Spring's component scanning.
 */
@Component
public class JWTUtils {
    /**
     * The secret key (in String form) used for signing the JWTs.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * The expiration time (in milliseconds) for access tokens.
     */
    @Value("${jwt.expirationTokenMs}")
    private long expirationToken;

    /**
     * The expiration time (in milliseconds) for refresh tokens.
     */
    @Value("${jwt.expirationRefreshTokenMs}")
    private long expirationRefreshToken;

    /**
     * The {@link SecretKey} instance used to sign the JWTs.
     * It is initialized using the secret and the HMAC-SHA-512 algorithm.
     */
    private final SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");


    /**
     * Generates an access token for the given user.
     *
     * @param user The {@link User} for whom the token is being generated.
     * @return A signed JWT access token.
     */
    public String generateAccessToken(User user){
        return generateToken(user,expirationToken);
    }

    /**
     * Generates a refresh token for the given user.
     *
     * @param user The {@link User} for whom the token is being generated.
     * @return A signed JWT refresh token.
     */
    public String generateRefreshToken(User user){
        return generateToken(user,expirationRefreshToken);
    }

    /**
     * Generates a token for the given user with the specified expiration time.
     *
     * @param user The {@link User} for whom the token is being generated.
     * @param expiration The expiration time (in milliseconds) for the token.
     * @return A signed JWT.
     */
    private String generateToken(User user,long expiration) {
        return Jwts.builder()
                .claims(Jwts.claims()
                        .subject(user.getUsername())
                        .issuedAt(new Date())
                        .expiration(new Date(new Date().getTime()+expiration))
                        .add("roles",user.getAuthorities())
                        .build())
                .signWith(key, Jwts.SIG.HS512).compact();
    }
}
