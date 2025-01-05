package be.helha.api_recettapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an evaluation of a recipe by a user in a contest entry.
 * An evaluation includes a rate, a comment, the associated user, recipe, and entry details,
 * as well as any associated images and the date of the evaluation.
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
     * The rating given by the user for the recipe.
     * This value should be between 0 and 5.
     */
    private int rate;

    /**
     * The comment provided by the user about the recipe.
     */
    private String commentaire;

    /**
     * The contest entry associated with this evaluation.
     * This relationship is mandatory and links the evaluation to a specific contest entry.
     */
    @ManyToOne
    @JoinColumn(name = "entry_id", nullable = false)
    private Entry entry;

    /**
     * The recipe that is being evaluated.
     * This is a mandatory field in the evaluation.
     */
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    /**
     * List of images associated with the evaluation.
     * The images are mapped by the "evaluation" field in the {@link ImageData} class.
     * This field is ignored during JSON serialization.
     */
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ImageData> images;

    /**
     * The date and time when the evaluation was created or submitted.
     */
    private LocalDateTime dateEvaluation;
}
