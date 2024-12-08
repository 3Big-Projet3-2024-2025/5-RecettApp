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
     * No-arguments constructor.
     * Required by JPA or to create an empty instance.
     */
    public ContestCategory() {
    }

    /**
     * All-arguments constructor.
     * Initializes all attributes of the class.
     *
     * @param id          Unique identifier for the category.
     * @param title       Title of the category.
     * @param description Description of the category.
     */
    public ContestCategory(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
