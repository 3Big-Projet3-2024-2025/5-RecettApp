package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.RecipeComponent;
import be.helha.api_recettapp.repositories.jpa.RecipeComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeComponentServiceDB implements IRecipeComponent{
    @Autowired
    private RecipeComponentRepository recipeComponentRepository;
    /**
     * Retrieves a paginated list of recipe components.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link RecipeComponent} objects.
     */
    @Override
    public Page<RecipeComponent> getRecipeComponents(Pageable page) {
        return null;
    }

    /**
     * Retrieves a list of all recipe components.
     *
     * @return a {@link List} of {@link RecipeComponent} objects.
     */
    @Override
    public List<RecipeComponent> getRecipeComponents() {
        return List.of();
    }

    /**
     * Adds a new recipe component to the system.
     *
     * @param recipeComponent the {@link RecipeComponent} object to add.
     * @return the added {@link RecipeComponent}.
     */
    @Override
    public RecipeComponent addRecipeComponent(RecipeComponent recipeComponent) {
        return null;
    }

    /**
     * Updates an existing recipe component.
     *
     * @param recipeComponent the {@link RecipeComponent} object with updated information.
     * @return the updated {@link RecipeComponent}.
     */
    @Override
    public RecipeComponent updateRecipeComponent(RecipeComponent recipeComponent) {
        return null;
    }

    /**
     * Deletes a recipe component by its ID.
     *
     * @param id the ID of the {@link RecipeComponent} to delete.
     */
    @Override
    public void deleteRecipeComponent(int id) {

    }

    /**
     * Retrieves a recipe component by its ID.
     *
     * @param id the ID of the {@link RecipeComponent} to retrieve.
     * @return the requested {@link RecipeComponent}.
     */
    @Override
    public RecipeComponent getRecipeComponentById(int id) {
        return null;
    }
}
