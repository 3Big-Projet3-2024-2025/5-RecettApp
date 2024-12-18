package be.helha.api_recettapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import be.helha.api_recettapp.security.KeycloakRoleConverter;


/**
 * @author Diesbecq Aaron
 *
 * Configuration class for Spring Security.
 * This class uses annotation {@code @Configuration} to indicate that this class provides Spring configuration.
 * It uses annotation {@code @EnableWebSecurity} to enable Spring Security’s web security support, allowing for customizing authentication and authorization.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * A service that provides user details for authentication.
     * The annotation {@code @Autowired} is used to automatically injected by Spring's dependency injection framework.
     */
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * Configures the Spring Security filter chain for the application.
     * This method defines the security rules for handling HTTP requests, including enabling or disabling
     * CSRF protection and configuring authorization requirements for different endpoints.
     *
     * The function use {@code @Bean} annotation to indicate that this method produces a Spring bean, which will be managed by the Spring container.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                    // Permits all requests to all endpoints for the moment, to easily test API
                    //authorizeRequests.requestMatchers("/api/users").authenticated();
                    authorizeRequests.anyRequest().permitAll();
                }).oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                ).build();
    }

    /**
     * Bean definition for the password encoder used to encode passwords.
     *
     * @return A {@link PasswordEncoder} bean (specifically {@link BCryptPasswordEncoder}).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Bean definition for the authentication manager used to authenticate users.
     *
     * @param http The {@link HttpSecurity} instance, used to configure HTTP security settings.
     * @param passwordEncoder The {@link PasswordEncoder} used to validate passwords.
     * @return An {@link AuthenticationManager} bean, used to authenticate users based on their credentials.
     * @throws Exception If there is an error while configuring the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuiler = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuiler.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuiler.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}
