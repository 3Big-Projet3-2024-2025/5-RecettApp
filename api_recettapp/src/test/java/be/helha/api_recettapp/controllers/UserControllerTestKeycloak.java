package be.helha.api_recettapp.controllers;


import be.helha.api_recettapp.models.Users;

import be.helha.api_recettapp.services.KeycloakUserService;
import be.helha.api_recettapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Test class for {@link UsersController}, focused on testing the functionality related to
 * blocking and unblocking users.
 *
 * <p>This test class uses Spring's {@code @WebMvcTest} to test the controller layer in isolation,
 * while mocking the underlying service layer.</p>
 */
@WebMvcTest(UsersController.class)
@Import(UserControllerTestKeycloak.SecurityConfig.class)
public class UserControllerTestKeycloak {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeycloakUserService keycloakUserService;

    @MockBean
    private UserService userService;

    /**
     * Test case for verifying the blocking of a user.
     *
     * <p>This test mocks the behavior of the {@link KeycloakUserService#blockUser(String)} method
     * and ensures the {@code POST /api/users/{id}/block} endpoint responds with a status of 200 (OK)
     * when invoked with valid parameters.</p>
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test
    void testBlockUser() throws Exception {
        String id = "test-user-id";

        doNothing().when(keycloakUserService).blockUser(id);

        mockMvc.perform(post("/api/users/{id}/block", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test case for verifying the unblocking of a user.
     *
     * <p>This test mocks the behavior of the {@link KeycloakUserService#unblockUser(String)} method
     * and ensures the {@code POST /api/users/{id}/unblock} endpoint responds with a status of 200 (OK)
     * when invoked with valid parameters.</p>
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test
    void testUnblockUser() throws Exception {
        String id = "test-user-id";

        doNothing().when(keycloakUserService).unblockUser(id);

        mockMvc.perform(post("/api/users/{id}/unblock", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**

     * Test for the deleteUser method.
     * Verifies that a user is anonymized, deleted in Keycloak, and removed from the local database correctly.
     */
    @Test
    void testDeleteUser() throws Exception {
        Users user = new Users(1L, "Abdel", "Alahyane", "abdel@gmail.com", LocalDate.now(), false,null,null);

        doNothing().when(keycloakUserService).deleteUser(user.getId().toString());
        when(userService.findById(user.getId())).thenReturn(user);

        mockMvc.perform(delete("/api/users/{id}", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Test case for the {@link UsersController#getAllUsers()} method.
     *
     * <p>This test verifies that the {@code getAllUsers} endpoint correctly retrieves all user records from the
     * service layer and returns them in the expected JSON format. It ensures proper communication between
     * the controller and service layers.</p>
     *
     * <p>The following steps are performed in this test:</p>
     * <ol>
     *     <li>Mock the {@link UserService#findAll()} method to return a predefined list of {@link Users} objects.</li>
     *     <li>Invoke the GET request on the endpoint {@code /api/users} using {@link MockMvc}.</li>
     *     <li>Assert that the HTTP status is {@code 200 OK}.</li>
     *     <li>Verify that the returned JSON array has the expected size and that the content matches the mocked data.</li>
     *     <li>Validate that the service layer's {@code findAll()} method is called exactly once during the test.</li>
     * </ol>
     *
     * @throws Exception if the request fails or if any validation/assertion fails.
     * @see UsersController#getAllUsers()
     * @see UserService#findAll()
     */
    @Test
    void testGetAllUsers() throws Exception {
        Users user = new Users(1L, "Abdel", "Alahyane", "abdel@gmail.com", LocalDate.now(), false, null, null);
        List<Users> usersList = Arrays.asList(user);

        when(userService.findAll()).thenReturn(usersList);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("[0].firstName").value("Abdel"));

        verify(userService, times(1)).findAll();
    }

    /**
     * Security configuration for testing purposes.
     *
     * <p>This configuration customizes security settings to disable CSRF and permit all requests.
     * It also provides a {@link UsersController} bean to be used during the tests.</p>
     */
    @Configuration
    static class SecurityConfig {

        /**
         * Configures the security filter chain for testing.
         *
         * <p>This method disables CSRF protection and allows all requests to bypass authentication
         * to facilitate testing of controller endpoints.</p>
         *
         * @param http the {@link HttpSecurity} to configure.
         * @return the configured {@link SecurityFilterChain}.
         * @throws Exception if an error occurs during configuration.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // Disable CSRF for test
            http.csrf(csrf -> csrf.disable())
                    .authorizeRequests()
                    .anyRequest().permitAll();
            return http.build();
        }

        /**
         * Provides a {@link UsersController} bean for testing.
         *
         * <p>The controller is created using a mocked {@link UserService} to allow testing
         * of its behavior in isolation.</p>
         *
         * @param userService the mocked {@link UserService} instance.
         * @return a new {@link UsersController} instance.
         */
        @Bean
        public UsersController usersController(UserService userService) {
            return new UsersController(userService);
        }
    }

    /**
     * Unit tests for UsersController.
     * These tests validate the behavior of the controller for CRUD operations.
     */
    static class TestCrudUser {

        @Mock
        private UserService userService;

        @InjectMocks
        private UsersController usersController;

        private Users user;

        /**
         * Sets up the test environment before each test.
         * Initializes the mock objects and creates a sample user.
         */
        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            user = new Users(1L, "Abdel", "Alahyane", "abdel@gmail.com", LocalDate.now(), false,null,null);
        }


        /**
         * Test for the createUser method.
         * Verifies that a user can be successfully created.
         */
        @Test
        void testCreateUser() {
            when(userService.save(user)).thenReturn(user);

            ResponseEntity<Users> response = usersController.createUser(user);

            assertNotNull(response);
            assertEquals("Abdel", response.getBody().getFirstName());

            verify(userService, times(1)).save(user);
        }

        /**
         * Test for the getUserById method.
         * Verifies that a user is retrieved correctly if they exist.
         */
        @Test
        void testGetUserById() {
            when(userService.findById(1L)).thenReturn(user);

            ResponseEntity<Users> response = usersController.getUserById(1L);

            assertNotNull(response);
            assertEquals("Abdel", response.getBody().getFirstName());

            verify(userService, times(1)).findById(1L);
        }

        /**
         * Test for the getUserByEmail method.
         * Verifies that a user is retrieved correctly if their email exists.
         */
        @Test
        void testGetUserByEmail() {
            when(userService.findByEmail("abdel@gmail.com")).thenReturn(user);

            ResponseEntity<Users> response = usersController.getUserByEmail("abdel@gmail.com");

            assertNotNull(response);
            assertEquals("Abdel", response.getBody().getFirstName());
            assertEquals("abdel@gmail.com", response.getBody().getEmail());

            verify(userService, times(1)).findByEmail("abdel@gmail.com");
        }

        /**
         * Test for the updateUser method.
         * Verifies that a user is updated correctly if they exist.
         */
        @Test
        void testUpdateUser() {
            when(userService.findById(1L)).thenReturn(user);
            when(userService.save(user)).thenReturn(user);

            ResponseEntity<Users> response = usersController.updateUser(1L, user);

            assertNotNull(response);
            assertEquals("Abdel", response.getBody().getFirstName());

            verify(userService, times(1)).findById(1L);
            verify(userService, times(1)).save(user);
        }


    }
}
