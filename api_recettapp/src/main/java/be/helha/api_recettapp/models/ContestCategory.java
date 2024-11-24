package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Data;


/**
 * Represents a contest category.
 * This class contains the basic information
 * needed to define a contest category.
 * It uses Lombok to automatically generate
 * getters, setters, and utility methods.
 */
@Data
@Entity
public class ContestCategory {

    /**
     * Unique identifier for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the category.
     */
    private String title;

    /**
     * Optional description of the category.
     * Provides additional details about the category.
     */
    private String description;

    /**
     * Status of the category (active or inactive).
     */
    private boolean status;

    /**
     * No-arguments constructor.
     * Required by JPA or to create an empty instance.
     */
    public ContestCategory() {
        // Default, empty constructor
    }

    /**
     * All-arguments constructor.
     * Initializes all attributes of the class.
     *
     * @param id          Unique identifier for the category.
     * @param title       Title of the category.
     * @param description Description of the category.
     * @param status      Status of the category.
     */
    public ContestCategory(Long id, String title,
                           String description, boolean status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
