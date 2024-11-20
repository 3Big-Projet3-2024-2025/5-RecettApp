package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;
/**
 * @author Demba Mohamed Samba
 * @description:  Represents a recipe entity in the RecettApp application.
 * This entity includes details about a recipe, such as its title, description,
 * category, preparation and cooking times, difficulty level, and more.
 *
 *

 * @With Lombok annotations that automatically generate getter and setter methods
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Title of the recipe.
     * This field is mandatory.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Detailed description of the recipe.
     * Stored as a Large Object (LOB).
     */
    @Column(nullable = false)
    @Lob
    private String description;

    /**
     * Category of the recipe (e.g., Dessert, Main Course, Appetizer).
     */
    @Column(nullable = false)
    private String category;

    /**
     * Preparation time for the recipe, in minutes.
     */
    @Column(nullable = false)
    private int preparationTime;

    /**
     * Cooking time for the recipe, in minutes.
     */
    @Column(nullable = false)
    private int cookingTime;

    /**
     * Number of servings the recipe yields.
     */
    @Column(nullable = false)
    private int servings;

    /**
     * Difficulty level of the recipe (e.g., Easy, Medium, Hard).
     */
    @Column(nullable = false)
    private String difficultyLevel;

    /**
     * Instructions for preparing the recipe.
     * Stored as a Large Object (LOB).
     */
    @Column(nullable = false)
    @Lob
    private String instructions;

    /**
     * URL pointing to the image of the recipe.
     */
    private String photoUrl;

    /**
     * Indicates whether the recipe has been approved for publishing.
     */
    @Column(nullable = false)
    private boolean approved;

    /**
     * Identifier for the type of recipe.
     */
    @Column(nullable = false)
    private int recipeTypeId;

    /**
     * Identifier indicating whether the recipe is part of a contest.
     */
    @Column(nullable = false)
    private int inContestId;

    /**
     * List of components (ingredients) used in the recipe.
     * Mapped by the "recipe" field in the {@link RecipeComponent} class.
     */
    @OneToMany(mappedBy = "recipe")
    private List<RecipeComponent> components;

    /*
    waiting for the other classes to be init
    @ManyToOne
    @JoinColumn(name = "recipe_type_id", nullable = false)
    private RecipeType recipeType;



    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
     */


}
