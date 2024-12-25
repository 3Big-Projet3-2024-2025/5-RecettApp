package be.helha.api_recettapp.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Demba Mohamed Samba
 * @description: Represents a component of a recipe, including its quantity and the associated ingredient.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeComponent {

    /**
     * Unique identifier for the RecipeComponent.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="RECIPE_ID",nullable = false)
    private Recipe recipe;

    /**
     * Quantity of the ingredient used in the recipe.
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * The ingredient associated with this recipe component.
     * This relationship is defined as a one-to-one association.
     */

    /**
     * Many-to-One relationship with ingredients.
     * A RecipeComponent can have multiple Ingredients.
     */
    @ManyToOne
    @JoinColumn(name="Ingredient_ID",nullable = false)
    public Ingredient ingredient;


    /**
     * Unit of the ingredient used in the recipe.
     */
    @Column(nullable = false)
    private String unit;
}
