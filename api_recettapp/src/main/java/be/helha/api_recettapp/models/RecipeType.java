package be.helha.api_recettapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String name;
}
