package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.Role;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles business logic for managing Users.
 * Provides methods for CRUD operations (Create, Read, Update, Delete).
 *
 * This class implements the {@link UserDetailsService} interface, which is used by Spring Security to
 * load user-specific data during authentication.
 */
@Service

public class UserService implements IUserService, UserDetailsService{


    /**
     * Repository for accessing user data from the database.
     * The annotation {@code @Autowired} is used to automatically injected by Spring's dependency injection framework.
     */
    @Autowired
    private final UserRepository userRepository;

    /**
     * Loads a user by their email and converts their roles to granted authorities.
     *
     * @param email The email of the user to load.
     * @return A {@link UserDetails} object containing the user's authentication and authorization information.
     * @throws UsernameNotFoundException If no user with the given email is found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),getGrantedAuthorities(user.getRoles()));
        return userDetails;
    }

    /**
     * Converts a list of user roles to a list of Spring Security {@link GrantedAuthority} objects.
     * Each role is prefixed with "ROLE_" and converted to uppercase to comply with Spring Security conventions.
     *
     * @param roles The list of {@link Role} objects representing the user's roles.
     * @return A list of {@link GrantedAuthority} objects corresponding to the user's roles.
     */
    public List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getTitle().toUpperCase()));
        }
        return authorities;
    }

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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    /**
     * Deletes a user from the database by their ID.
     * @param id the ID of the user to delete
     */
    public void delete(Long id) {
        userRepository.deleteById((long) Math.toIntExact(id));
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
