package be.helha.api_recettapp.models;

import jakarta.persistence.*;

/**
 * This class represents an evaluation of a recipe by a participant of a contest (Entry)
 */
@Entity
public class Evaluation {
    /**
     * Identifier of an evaluation
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The recipe evaluated
     */
    @OneToOne
    private Recipe recipe;

    /**
     * The Entry that give the rate
     */
    @OneToOne
    private Entry entry;

    /**
     * A rate between 0 and 5
     */
    private int rate;
}
