package be.helha.api_recettapp.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a User entity in the RecettApp application.
 * This class models a user with various attributes such as name, email, and roles.
 * It also manages relationships with entries and roles within the application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {

    /**
     * Unique identifier for the user.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * First name of the user.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Last name of the user.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Email address of the user.
     * This field cannot be null and must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Password of the user.
     * This field cannot be null.
     * For security, passwords should be encrypted.
     */

    @Column(nullable = false)
    private String password;

    /**
     * Status of the user (e.g., active, inactive).
     */
    private String status;

    /**
     * Date of registration for the user.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private LocalDate date_registration;

    /**
     * Phone number of the user.
     */
    private String phone_number;


    /**
     * List of entries (inscriptions) associated with the user.
     * One user can have multiple entries.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Entry> registrations;

    /**
     * List of roles associated with the user.
     * One user can have multiple roles.
     */

    @OneToMany(mappedBy = "users")
    private List<Role> roles;


}
