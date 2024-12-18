package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "Title is mandatory.")
    @Size(max = 255, message = "Title cannot exceed 255 characters.")
    private String title;

    /**
     * Optional description of the category.
     * Provides additional details about the category.
     * Cannot exceed 255 characters.
     */
    @Size(max = 255, message = "Description cannot exceed 255 characters.")
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
