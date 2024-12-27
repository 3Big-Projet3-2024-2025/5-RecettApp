package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.services.IUserService;
import be.helha.api_recettapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing Users.
 * Provides CRUD endpoints for creating, reading, updating, and deleting Users.
 */
@RestController
@RequestMapping("/api/users")
public class UsersController {


    @Autowired
    private final UserService userService;

    /**
     * Constructor for UsersController.
     * Initializes the controller with a UserService instance.
     *
     * @param userService the service used for business logic related to Users
     */
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing a list of all users and an HTTP status of 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing the user if found, or a 404 (Not Found) status if the user does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a new user.
     *
     * @param user the user data to create
     * @return a ResponseEntity containing the created user and an HTTP status of 200 (OK)
     */
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        return ResponseEntity.ok(userService.save(user));
    }

    /**
     * Updates an existing user.
     *
     * @param id the ID of the user to update
     * @param user the updated user data
     * @return a ResponseEntity containing the updated user and an HTTP status of 200 (OK),
     *         or a 404 (Not Found) status if the user does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users user) {
        Users existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        return ResponseEntity.ok(userService.save(user));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity with an HTTP status of 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return a ResponseEntity containing the user if found, or a 404 (Not Found) status if the user does not exist
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
        Users user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

}
