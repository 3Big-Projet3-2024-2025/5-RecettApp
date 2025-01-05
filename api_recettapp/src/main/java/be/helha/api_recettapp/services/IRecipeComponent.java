package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.RecipeComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing recipe components.
 * Provides methods to retrieve, add, update, and delete recipe components.
 *
 * <p>
 * This interface acts as the abstraction layer between the controller
 * and the data access logic for {@link RecipeComponent}.
 * </p>
 *
 * @author Demba Mohamed Samba
 */
public interface IRecipeComponent {

    /**
     * Retrieves a paginated list of recipe components.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link RecipeComponent} objects.
     */
    public Page<RecipeComponent> getRecipeComponents(Pageable page);

    /**
     * Retrieves a list of all recipe components.
     *
     * @return a {@link List} of {@link RecipeComponent} objects.
     */
    public List<RecipeComponent> getRecipeComponents();

    /**
     * Adds a new recipe component to the system.
     *
     * @param recipeComponent the {@link RecipeComponent} object to add.
     * @return the added {@link RecipeComponent}.
     */
    public RecipeComponent addRecipeComponent(RecipeComponent recipeComponent);

    /**
     * Updates an existing recipe component.
     *
     * @param recipeComponent the {@link RecipeComponent} object with updated information.
     * @return the updated {@link RecipeComponent}.
     */
    public RecipeComponent updateRecipeComponent(RecipeComponent recipeComponent);

    /**
     * Deletes a recipe component by its ID.
     *
     * @param id the ID of the {@link RecipeComponent} to delete.
     */
    public void deleteRecipeComponent(int id);

    /**
     * Retrieves a recipe component by its ID.
     *
     * @param id the ID of the {@link RecipeComponent} to retrieve.
     * @return the requested {@link RecipeComponent}.
     */
    public RecipeComponent getRecipeComponentById(int id);

    /**
     * Retrieves a list of recipe components that contain the specified recipe.
     *
     * @param recipeId the ID of the recipe for which the components need to be retrieved.
     * @return a list of {@link RecipeComponent} associated with the specified recipe.
     */
    public  List<RecipeComponent> findRecipeComponentsByRecipeId( int recipeId);
}
