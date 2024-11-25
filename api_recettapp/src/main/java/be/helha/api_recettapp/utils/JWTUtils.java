package be.helha.api_recettapp.utils;

import io.jsonwebtoken.JwtException;
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
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        return Jwts.builder()
                .claims(Jwts.claims()
                        .subject(user.getUsername())
                        .issuedAt(new Date())
                        .expiration(new Date(new Date().getTime()+expiration))
                        .add("roles",user.getAuthorities())
                        .build())
                .signWith(key, Jwts.SIG.HS512).compact();
    }

    /**
     * Validates the provided JWT.
     *
     * @param token The JWT to be validated.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }

    /**
     * Parses the provided JWT and extracts its claims.
     *
     * @param token The JWT to be parsed.
     * @throws JwtException if the token is invalid or cannot be parsed.
     */
    public void parseToken(String token) throws JwtException {
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
