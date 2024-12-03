package be.helha.api_recettapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author Demba Mohamed Samba
 *
 * @description: Represents an ingredient with its nutritional information.
 */
@Getter
@Setter
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
    private int alimentSpecifyGroupCode;

    /**
     * Code of the food group.
     */
    @Column(nullable = false)
    private int alimentGroupCode;

    /**
     * Name of the food group.
     */
    @Column(nullable = false)
    private String alimentGroupName;

    /**
     * Name of the food item.
     */
    @Column(nullable = false)
    private String alimentName;

    /**
     * Name specifying the subgroup of the food item.
     */
    @Column(nullable = false)
    private String alimentSpecifyGroupName;

    @OneToMany(mappedBy = "ingredient")
    @JsonIgnore
    private List<RecipeComponent> recipeComponent;

}
