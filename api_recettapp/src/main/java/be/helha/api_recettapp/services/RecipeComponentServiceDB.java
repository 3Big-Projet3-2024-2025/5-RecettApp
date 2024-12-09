package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.RecipeComponent;
import be.helha.api_recettapp.repositories.jpa.RecipeComponentRepository;
import be.helha.api_recettapp.repositories.jpa.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for managing {@link RecipeComponent} entities, providing CRUD operations and database interactions.
 * @author Demba Mohamed Samba
 */
@Service
@Primary
public class RecipeComponentServiceDB implements IRecipeComponent {

    @Autowired
    private RecipeComponentRepository recipeComponentRepository;
    @Autowired
    private RecipeServiceDB recipeServiceDB;
    @Autowired
    private IngredientServiceDB ingredientServiceDB;


    /**
     * Retrieves a paginated list of recipe components.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link RecipeComponent} objects.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public Page<RecipeComponent> getRecipeComponents(Pageable page) {
        try {
            return recipeComponentRepository.findAll(page);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recipe components: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of all recipe components.
     *
     * @return a {@link List} of {@link RecipeComponent} objects.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public List<RecipeComponent> getRecipeComponents() {
        try {
            return recipeComponentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recipe components: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a new recipe component to the system.
     *
     * @param recipeComponent the {@link RecipeComponent} object to add.
     * @return the added {@link RecipeComponent}.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public RecipeComponent addRecipeComponent(RecipeComponent recipeComponent) {
        try {
            if (recipeComponentRepository.existsByRecipeIdAndIngredientId(
                    recipeComponent.getRecipe().getId(),
                    recipeComponent.getIngredient().getId())) {
                throw new RuntimeException("Recipe component with the same recipe and ingredient already exists.");
            }
            if (recipeServiceDB.getRecipeById(recipeComponent.getRecipe().getId()) == null) {
                throw new NoSuchElementException("Recipe with the " + recipeComponent.getRecipe().getId() + " not found in the database.");
            }if (ingredientServiceDB.getIngredientById(recipeComponent.getIngredient().getId()) == null) {
                throw new NoSuchElementException("Ingredient with the " + recipeComponent.getIngredient().getId() + " not found in the database.");
            }
            return recipeComponentRepository.save(recipeComponent);
        } catch (Exception e) {
            throw new RuntimeException("Error adding recipe component: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing recipe component.
     *
     * @param recipeComponent the {@link RecipeComponent} object with updated information.
     * @return the updated {@link RecipeComponent}.
     * @throws NoSuchElementException if the recipe component with the given id does not exist.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public RecipeComponent updateRecipeComponent(RecipeComponent recipeComponent) {
        if (!recipeComponentRepository.existsById(recipeComponent.getId())) {
            throw new NoSuchElementException("RecipeComponent with id " + recipeComponent.getId() + " not found");
        }
        try {
            return recipeComponentRepository.save(recipeComponent);
        } catch (Exception e) {
            throw new RuntimeException("Error updating recipe component: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a recipe component by its id.
     *
     * @param id the id of the {@link RecipeComponent} to delete.
     * @throws NoSuchElementException if the recipe component with the given id does not exist.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public void deleteRecipeComponent(int id) {
        if (!recipeComponentRepository.existsById(id)) {
            throw new NoSuchElementException("RecipeComponent with id " + id + " not found");
        }
        try {
            recipeComponentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting recipe component: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a recipe component by its id.
     *
     * @param id the id of the {@link RecipeComponent} to retrieve.
     * @return the requested {@link RecipeComponent}.
     * @throws NoSuchElementException if the recipe component with the given id does not exist.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public RecipeComponent getRecipeComponentById(int id) {
        return recipeComponentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("RecipeComponent with id " + id + " not found"));
    }
}
