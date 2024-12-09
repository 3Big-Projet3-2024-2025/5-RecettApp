package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an evaluation of a recipe by a user in a contest entry.
 * An evaluation includes a rate, the associated user, recipe, and entry details.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Evaluation {
    /**
     * Unique identifier for the evaluation.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The contest entry associated with this evaluation.
     * Each evaluation corresponds to a single entry.
     */
    @OneToOne
    private Entry entry;

    /**
     * The rating given by the user for the recipe.
     * This value should be between 0 and 5.
     */
    private int rate;

    /**
     * The user who provided the evaluation.
     * This is a mandatory field in the evaluation.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /**
     * The recipe that is being evaluated.
     * This is a mandatory field in the evaluation.
     */
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
