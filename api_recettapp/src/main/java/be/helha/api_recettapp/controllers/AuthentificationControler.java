package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.JWT;
import be.helha.api_recettapp.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Diesbecq Aaron
 *
 * Controller class responsible for authentication-related operations.
 * This class uses annotation {@code @RestController} to indicate that this class is a Spring MVC controller where each method's return value is directly written to the HTTP response body.
 * It uses annotation {@code @RequestMapping("/auth")} to map HTTP requests to the "/auth" URL path to methods in this controller.
 */
@RestController
@RequestMapping("/auth")
public class AuthentificationControler {

    /**
     * Utility class for handling JWT operations.
     * The annotation {@code @Autowired} is used to automatically injected by Spring's dependency injection framework.
     */
    @Autowired
    JWTUtils jwtUtils;

    /**
     * Authentication manager for processing user authentication requests.
     * The annotation {@code @Autowired} is used to automatically injected by Spring's dependency injection framework.
     */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Controller method for handling user authentication and returning JWT tokens.
     *
     * The annotation {@code @PostMapping("login")} is used to map HTTP POST requests to the "/login" URL path to this method.
     * This endpoint is used to authenticate users.
     */
    @PostMapping("login")
    public ResponseEntity<?> authenticate(@RequestParam String username, @RequestParam String password) {
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            User user = (User)auth.getPrincipal();
            JWT jwt = new JWT(jwtUtils.generateAccessToken(user),jwtUtils.generateRefreshToken(user));
            return ResponseEntity.ok(jwt);
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
    }
}
