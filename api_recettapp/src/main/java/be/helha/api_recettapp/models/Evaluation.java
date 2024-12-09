package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents an evaluation of a recipe by a participant of a contest (Entry)
 */


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Evaluation {
    /**
     * Identifier of an evaluation
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




    /**
     * The Entry that give the rate
     */
    @OneToOne
    private Entry entry;

    /**
     * A rate between 0 and 5
     */
    private int rate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /**
     * The recipe evaluated
     */
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
