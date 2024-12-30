package be.helha.api_recettapp;

import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import be.helha.api_recettapp.controllers.UsersController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UsersController.
 * These tests validate the behavior of the controller for CRUD operations.
 */
class TestCrudUser {

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
     * Test for the getAllUsers method.
     * Verifies that all users are retrieved correctly.
     */
    @Test
    void testGetAllUsers() {
        when(userService.findAll()).thenReturn(Arrays.asList(user));

        ResponseEntity<List<Users>> response = usersController.getAllUsers();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals("Abdel", response.getBody().get(0).getFirstName());

        verify(userService, times(1)).findAll();
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

    /**
     * Test for the deleteUser method.
     * Verifies that a user is deleted correctly.
     */
    @Test
    void testDeleteUser() {
        doNothing().when(userService).delete(1L);

        ResponseEntity<Void> response = usersController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());

        verify(userService, times(1)).delete(1L);
    }



}
