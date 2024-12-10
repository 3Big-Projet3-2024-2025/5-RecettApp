package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.repositories.jpa.ContestRepository;
import be.helha.api_recettapp.repositories.jpa.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RecipeServiceDB implements IRecipeService{

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private ContestService contestService;
    @Autowired
    private ContestCategoryService contestCategoryService;
    @Autowired
    private ContestRepository contestRepository;

    /**
     * Retrieves a paginated list of recipes.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link Recipe} objects.
     */
    @Override
    public Page<Recipe> getRecipes(Pageable page) {
        try {
            return recipeRepository.findAll(page);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recipes: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of all recipes.
     *
     * @return a {@link List} of {@link Recipe} objects.
     */
    @Override
    public List<Recipe> getRecipes() {
        try {
            return recipeRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recipes: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a new recipe to the system.
     *
     * @param recipe the {@link Recipe} object to add.
     * @return the added {@link Recipe} object.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public Recipe addRecipe(Recipe recipe) {

        try {
            if (recipe.getContest() == null) throw new RuntimeException("Contest is mandatory and cannot be null.");
            int idContest = recipe.getContest().getId();
            if (contestRepository.findById(idContest).isEmpty()) {
                throw new RuntimeException("Contest with ID " + idContest + " does not exist.");
            }
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new RuntimeException("Error adding recipe: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing recipe in the system.
     *
     * @param recipe the {@link Recipe} object to update.
     * @return the updated {@link Recipe} object.
     * @throws NoSuchElementException if the recipe with the given ID does not exist.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public Recipe updateRecipe(Recipe recipe) {
        if (!recipeRepository.existsById(recipe.getId())) {
            throw new NoSuchElementException("Recipe with ID " + recipe.getId() + " not found");
        }
        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new RuntimeException("Error updating recipe: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a recipe from the system by its ID.
     *
     * @param id the ID of the {@link Recipe} to delete.
     * @throws NoSuchElementException if the recipe with the given ID does not exist.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public void deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            throw new NoSuchElementException("Recipe with ID " + id + " not found");
        }
        try {
            recipeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting recipe: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of all recipes.
     *
     * @param id the ID of the {@link Recipe} to get.
     * @return a {@link Recipe} objects.
     * @throws NoSuchElementException if the recipe with the given ID does not exist.
     */
    @Override
    public Recipe getRecipeById(int id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recipe with ID " + id + " not found"));
    }

}
