package be.helha.api_recettapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
     * Configures the Spring Security filter chain for the application.
     * This method defines the security rules for handling HTTP requests, including enabling or disabling
     * CSRF protection and configuring authorization requirements for different endpoints.
     *
     * The function use {@code @Bean} annotation to indicate that this method produces a Spring bean, which will be managed by the Spring container.
     * @param http the http element
     * @throws Exception if the token is not provided
     * @return securityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/api/contest-categories/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/api/contest-categories/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/api/contest-categories/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/api/contest-categories/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/api/contest-categories/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/contests/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/contests/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/contests/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/contests/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/entries/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/entries/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/entries/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/entries/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/api/evaluations/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/api/evaluations/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/api/evaluations/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/api/evaluations/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/ingredients/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/ingredients/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/ingredients/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/ingredients/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/recipe/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/recipe/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/recipe/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/recipe/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/api/recipe-types/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/api/recipe-types/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/api/recipe-types/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/api/recipe-types/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/api/users/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/api/users/{email}/unblock").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/api/users/{email}/block").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/api/users/*").hasRole("ADMIN");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/api/users/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/api/users/{id}").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/paypal/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/paypal/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/image/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/image/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/recipe-components/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/recipe-components/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/recipe-components/*").hasAnyRole("ADMIN", "USER");
                    authorizeRequests.requestMatchers(HttpMethod.DELETE,"/recipe-components/*").hasAnyRole("ADMIN", "USER");

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
