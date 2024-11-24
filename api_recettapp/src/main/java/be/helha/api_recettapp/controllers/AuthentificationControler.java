package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
