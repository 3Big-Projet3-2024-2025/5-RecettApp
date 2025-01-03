package be.helha.api_recettapp.services;


import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
/**
 * Interface for managing recipes in the system.
 *@author Demba Mohamed Samba
 */

public interface IRecipeService {
    /**
     * Retrieves a paginated list of recipes.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link Recipe} objects.
     */
    public Page<Recipe> getRecipes(Pageable page);

    /**
     * Retrieves a list of all recipes.
     *
     * @return a {@link List} of {@link Recipe} objects.
     */
    public List<Recipe> getRecipes();

    /**
     * Adds a new recipe to the system.
     *
     * @param recipe the {@link Recipe} object to add.
     * @return the added {@link Recipe} object.
     */
    public Recipe addRecipe(Recipe recipe);

    /**
     * Updates an existing recipe in the system.
     *
     * @param recipe the {@link Recipe} object to update.
     * @return the updated {@link Recipe} object.
     */
    public Recipe updateRecipe(Recipe recipe);

    /**
     * Deletes a recipe from the system by its ID.
     *
     * @param id the ID of the {@link Recipe} to delete.
     *
     */
    public void deleteRecipe(int id);

    /**
     * Retrieves a list of all recipes.
     * @param id the ID of the {@link Recipe} to get.
     * @return a {@link Recipe} objects.
     */
    public Recipe getRecipeById(int id);

    /**
     * Retrieves all recipes that belong to a specific contest.
     *
     * @param idContest The ID of the contest.
     * @return A list of recipes associated with the contest.
     */
    List<Recipe> getRecipeByIdContest(int idContest);
    /**
     * Retrieves paginated recipes created by a specific user.
     *
     * @param userMail the unique identifier of the user.
     * @param page and size The pagination information.
     * @return A paginated list of recipes.
     */
    public Page<Recipe> getRecipeByUserMail(String userMail, int page, int size) ;
    /**
     * Sets the "masked" field of a recipe to true, effectively anonymizing it.
     *
     * @param recipeId The ID of the recipe to anonymize.
     */
    public boolean anonymizeRecipe(int recipeId);

}
