package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.services.KeycloakUserService;
import be.helha.api_recettapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
@Import(UserControllerTestKeycloak.SecurityConfig.class)
public class UserControllerTestKeycloak {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeycloakUserService keycloakUserService;

    @MockBean
    private UserService userService;

    @Test
    void testBlockUser() throws Exception {
        String id = "test-user-id";

        doNothing().when(keycloakUserService).blockUser(id);

        mockMvc.perform(post("/api/users/{id}/block", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUnblockUser() throws Exception {
        String id = "test-user-id";

        doNothing().when(keycloakUserService).unblockUser(id);

        mockMvc.perform(post("/api/users/{id}/unblock", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Configuration class to personalize security
    @Configuration
    static class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // Disable CSRF for test
            http.csrf(csrf -> csrf.disable())
                    .authorizeRequests()
                    .anyRequest().permitAll();
            return http.build();
        }

        @Bean
        public UsersController usersController(UserService userService) {
            return new UsersController(userService);
        }
    }
}
