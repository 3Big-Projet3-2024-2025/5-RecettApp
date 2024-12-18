package be.helha.api_recettapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * RecipeType represents the type of recipe has such like : appetizer, desert, ...
 */
@Data
@Entity
public class RecipeType {
    /**
     *  Identifier of RecipeType
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Name of the type of the recipe
     */
    @NotBlank(message = "name is mandatory.")
    @Size(max = 255, message = "name cannot exceed 255 characters.")
    private String name;
}
