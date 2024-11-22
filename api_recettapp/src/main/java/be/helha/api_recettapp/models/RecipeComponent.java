package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Demba Mohamed Samba
 * @description: Represents a component of a recipe, including its quantity and the associated ingredient.
 */
@Entity
@Getter
@Setter
public class RecipeComponent {

    /**
     * Unique identifier for the RecipeComponent.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Quantity of the ingredient used in the recipe.
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * The ingredient associated with this recipe component.
     * This relationship is defined as a one-to-one association.
     */
    @OneToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
}
