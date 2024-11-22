package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Demba Mohamed Samba
 *
 * @description: Represents an ingredient with its nutritional information.
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor // Constructor with all arguments
@ToString
public class Ingredient {

    /**
     * Unique identifier for the ingredient.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Code specifying the subgroup of the food item.
     */
    @Column(nullable = false)
    private int AlimentSpecifyGroupCode;

    /**
     * Code of the food group.
     */
    @Column(nullable = false)
    private int AlimentGroupCode;

    /**
     * Name of the food group.
     */
    @Column(nullable = false)
    private String AlimentGroupName;

    /**
     * Name of the food item.
     */
    @Column(nullable = false)
    private String AlimentName;

    /**
     * Name specifying the subgroup of the food item.
     */
    @Column(nullable = false)
    private String AlimentSpecifyGroupName;

    /**
     * Energy in kilocalories per 100 grams.
     */
    @Column(nullable = false)
    private int Energy_kcal_100g;

}
