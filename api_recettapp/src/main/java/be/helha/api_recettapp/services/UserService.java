package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that handles business logic for managing Users.
 * Provides methods for CRUD operations (Create, Read, Update, Delete).
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     * @param userRepository the repository used for database operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the database.
     * @return a list of all users
     */
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     * @param id the ID of the user to find
     * @return the user if found, or null if no user exists with the given ID
     */
    public Users findById(Long id) {
        return userRepository.findById((long) Math.toIntExact(id)).orElse(null);
    }

    /**
     * Saves a user to the database.
     * Can be used to create a new user or update an existing user.
     * @param user the user to save
     * @return the saved user
     */
    public Users save(Users user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user from the database by their ID.
     * @param id the ID of the user to delete
     */
    public void delete(Long id) {
        userRepository.deleteById((long) Math.toIntExact(id));
    }

}
