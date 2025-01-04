package be.helha.api_recettapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
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
                    authorizeRequests.requestMatchers("/api/contest-categories").hasRole("USER");
                    authorizeRequests.anyRequest().permitAll();
                }).oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                ).build();
    }

    /**
     * Configures a {@link JwtAuthenticationConverter} to include custom role mappings from Keycloak.
     *
     * <p>This method sets up a {@link JwtAuthenticationConverter} and assigns it a custom
     * {@link JwtGrantedAuthoritiesConverter}, specifically the
     * {@link KeycloakRoleConverter}. The custom converter ensures that roles extracted from the JWT are
     * mapped to Spring Security granted authorities.</p>
     *
     * @return A configured {@link JwtAuthenticationConverter} instance.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}
