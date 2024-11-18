package be.helha.api_recettapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the role of a user.
 * This class is annotated with {@code @Entity}, indicating it is mapped to a database table.
 * It uses Lombok annotation {@code @Getter} to automatically generate all getter methods for each attribute.
 */
@Getter
@Entity
public class Role {

    /**
     * Unique identifier for the role.
     * It uses JPA annotation {@code @Id} to automatically signal at the database that is the primary key of the table.
     * It uses JPA annotation {@code @GeneratedValue(strategy = GenerationType.IDENTITY)} to automatically generate the primary key (id).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_role;

    /**
     * Name of the role.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private String title_role;
}
