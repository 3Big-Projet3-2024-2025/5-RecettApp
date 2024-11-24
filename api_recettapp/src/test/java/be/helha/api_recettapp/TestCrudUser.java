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

class UsersControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users(1L, "Abdel", "Alahyane", "abdel@gmail.com", "password", "active", LocalDate.now(), "0471772755");
    }


    @Test
    void testCreateUser() {
        when(userService.save(user)).thenReturn(user);

        ResponseEntity<Users> response = usersController.createUser(user);

        assertNotNull(response);
        assertEquals("Abdel", response.getBody().getFirstName());

        verify(userService, times(1)).save(user);
    }

    @Test
    void testGetAllUsers() {
        when(userService.findAll()).thenReturn(Arrays.asList(user));

        ResponseEntity<List<Users>> response = usersController.getAllUsers();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals("Abdel", response.getBody().get(0).getFirstName());

        verify(userService, times(1)).findAll();
    }



}
