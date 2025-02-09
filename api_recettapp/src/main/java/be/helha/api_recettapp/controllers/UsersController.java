package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.services.IUserService;
import be.helha.api_recettapp.services.KeycloakUserService;
import be.helha.api_recettapp.services.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for managing Users.
 * Provides CRUD endpoints for creating, reading, updating, and deleting Users.
 */
@RestController
@RequestMapping("/api/users")
public class UsersController {


    @Autowired
    private final UserService userService;

    @Autowired
    private KeycloakUserService keycloakUserService;

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
     * Retrieves all users without "Anonymized users" and without admins.
     *
     * @return a ResponseEntity containing a list of all users and an HTTP status of 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            // Get all users from the DB
            List<Users> allUsers = userService.findAll();

            // Get all users from Keycloak
            List<UserRepresentation> keycloakUsers = keycloakUserService.listUsers();

            // Filter in the Keycloak user list to get all admins in Keycloak
            Set<String> adminUserMails = keycloakUsers.stream()
                    .filter(user -> {
                        List<String> roles = user.getAttributes().get("roles");
                        return roles != null && roles.contains("admin");
                    }) // Verify if user as the "admin" role
                    .map(UserRepresentation::getEmail) // Get admins mails
                    .collect(Collectors.toSet());

            // Filter users without their with "Anonymized" FirstName and their with admin Keycloak role
            List<Users> filteredUsers = allUsers.stream()
                    .filter(user -> !user.getFirstName().equals("Anonymized") && !adminUserMails.contains(user.getEmail()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredUsers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing the user if found, or a 404 (Not Found) status if the user does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        try {
            Users user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Creates a new user.
     *
     * @param user the user data to create
     * @return a ResponseEntity containing the created user and an HTTP status of 200 (OK)
     */
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try {
            return ResponseEntity.ok(userService.save(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user to update
     * @param user the updated user data
     * @return a ResponseEntity containing the updated user and an HTTP status of 200 (OK),
     * or a 404 (Not Found) status if the user does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users user) {
        try {
            Users existingUser = userService.findById(id);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }
            user.setId(id);
            return ResponseEntity.ok(userService.save(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Get the user by its ID to anonymize it, suppress its account in Keycloak and the local DB.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity with an HTTP status of 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            Users user = userService.findById(id);
            keycloakUserService.deleteUser(user.getEmail()); //Suppress in Keycloak first because we need the email
            anonymizeUserData(user);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return a ResponseEntity containing the user if found, or a 404 (Not Found) status if the user does not exist
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
        try {
            Users user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Controller method for blocking a user.
     * <p>
     * This method handles a {@code POST} request to the {@code /{email}/block} path. It changes the
     * user's status to "blocked" both in Keycloak and in the database. The user is identified by
     * the {@code email} path variable.
     *
     * @param email The email of the user to be blocked.
     * @return A {@link ResponseEntity} with a success message indicating the user has been blocked.
     */
    @PostMapping("/{email}/block")
    public ResponseEntity<String> blockUser(@PathVariable String email) {
        try {
            //Change Keycloak status
            keycloakUserService.blockUser(email);

            //Change DB status
            Users localUser = getUserByEmail(email).getBody();
            if (localUser != null) {
                localUser.setBlocked(true);
                updateUser(localUser.getId(), localUser);
            }
            return ResponseEntity.ok("User blocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while blocking the user.");
        }
    }

    /**
     * Controller method for unblocking a user.
     * <p>
     * This method handles a {@code POST} request to the {@code /{email}/unblock} path. It changes
     * the user's status to "unblocked" both in Keycloak and in the database. The user is identified
     * by the {@code email} path variable.
     *
     * @param email The email of the user to be unblocked.
     * @return A {@link ResponseEntity} with a success message indicating the user has been unblocked.
     */
    @PostMapping("/{email}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable String email) {
        try {
            //Change Keycloak status
            keycloakUserService.unblockUser(email);

            //Change DB status
            Users localUser = getUserByEmail(email).getBody();
            if (localUser != null) {
                localUser.setBlocked(false);
                updateUser(localUser.getId(), localUser);
            }
            return ResponseEntity.ok("User unblocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while unblocking the user.");
        }
    }


    /**
     * Anonymizes the personal data of a given user.
     *
     * <p>This method replaces the user's first name with the string "Anonymized"
     * and sets their email address to a unique anonymized value using a randomly
     * generated UUID. The updated user data is then saved to the database.</p>
     *
     * @param user the {@link Users} object whose data needs to be anonymized.
     */
    public void anonymizeUserData(Users user) {
        try {
            String uuid = UUID.randomUUID().toString();
            user.setFirstName("Anonymized");
            user.setEmail("anonymized" + uuid + "@example.com");
            user.setBlocked(true);
            user.setRegistrations(null); // it's not necessary to keep the active entries if the user delete his account
            userService.save(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while anonymizing user data: " + e.getMessage(), e);
        }
    }


}
