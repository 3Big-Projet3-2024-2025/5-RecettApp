package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.*;
/**
 *
 *  Represents a recipe entity in the RecettApp application.
 * This entity includes details about a recipe, such as its title, description,
 * category, preparation and cooking times, difficulty level, and more.
 *
 * @author Demba Mohamed Samba
 */

/**
 * Lombok annotations that automatically generate getter and setter methods
 * for all fields in the class at compile time.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // A constructor with all args
@ToString
@Entity
public class Recipe {
    /**
     * Unique identifier for the recipe.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    private int id;

    @Column(nullable = false) // The name is mandatory
    private String title;

    @Column(nullable = false)
    @Lob // Large Object for detailed recipe
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int preparationTime; // In minutes

    @Column(nullable = false)
    private int cookingTime; // In minutes or type Date

    @Column(nullable = false)
    private int servings; // Number of servings

    @Column(nullable = false)
    private String difficultyLevel; // Example: Easy, Medium, Hard

    @Column(nullable = false)
    @Lob // Large Object for detailed preparation steps
    private String instructions;

    private String photoUrl; // URL for the recipe image

    @Column(nullable = false)
    private boolean approved;

    @Column(nullable = false)
    private int recipeTypeId;

    @Column(nullable = false)
    private int inContestId;

    /*
    waiting for the other classes to be init
    @ManyToOne
    @JoinColumn(name = "recipe_type_id", nullable = false)
    private RecipeType recipeType;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeComponent> components;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
     */


}
