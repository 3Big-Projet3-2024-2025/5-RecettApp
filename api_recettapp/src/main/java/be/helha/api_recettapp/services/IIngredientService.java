package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface for managing ingredients in the system.
 *@author Demba Mohamed Samba
 */
public interface IIngredientService {

    /**
     * Retrieves a paginated list of ingredients.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link Ingredient} objects.
     */
    public Page<Ingredient> getIngredients(Pageable page);

    /**
     * Retrieves a list of all ingredients.
     *
     * @return a {@link List} of {@link Ingredient} objects.
     */
    public List<Ingredient> getIngredients();
    /**
     * Retrieves a ingredient.
     * @param id the ID of the {@link Ingredient} to get.
     * @return a {@link Ingredient} objects.
     */
    public Ingredient getIngredientById( int id ) ;

    /**
     * Adds a new ingredient to the system.
     *
     * @param ingredient the {@link Ingredient} object to add.
     * @return the added {@link Ingredient} object.
     */
    public Ingredient addIngredient(Ingredient ingredient);

    /**
     * Updates an existing ingredient in the system.
     *
     * @param ingredient the {@link Ingredient} object to update.
     * @return the updated {@link Ingredient} object.
     */
    public Ingredient updateIngredient(Ingredient ingredient);

    /**
     * Deletes an ingredient from the system by its ID.
     *
     * @param id the ID of the {@link Ingredient} to delete.
     *
     */
    public void deleteIngredient(int id);

    /**
     * Searches ingredients dynamically based on a search term.
     *
     * @param searchTerm the term to search for in ingredient names.
     * @return a list of ingredients matching the search term.
     */
    public List<Ingredient> searchIngredients(String searchTerm);
}
