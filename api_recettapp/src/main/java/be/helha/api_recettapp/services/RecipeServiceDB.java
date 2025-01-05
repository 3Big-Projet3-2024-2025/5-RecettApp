package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.repositories.jpa.ContestRepository;
import be.helha.api_recettapp.repositories.jpa.EntryRepository;
import be.helha.api_recettapp.repositories.jpa.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The methods of the Service : RecipeServiceDB
 */
@Service
public class RecipeServiceDB implements IRecipeService{

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private ImageDataService imageDataService;

    /**
     * Retrieves a paginated list of recipes.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @param keyword the term search.
     * @return a {@link Page} of {@link Recipe} objects.
     */
    @Override
    public Page<Recipe> getRecipes(String keyword, Pageable page) {
        try {
            if (keyword == null || keyword.isEmpty()) {
            return recipeRepository.findAll(page);
        } else {
            return recipeRepository.findByKeyword(keyword, page);
        }
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
            if (recipe.getEntry() == null) throw new RuntimeException("Entry is mandatory and cannot be null.");
            int idEntry = recipe.getEntry().getId();
            try {
                if (entryRepository.findById(idEntry).isEmpty()) {
                    throw new RuntimeException("Entry with ID " + idEntry + " does not exist.");
                }
            }catch (NoSuchElementException e) {
                throw new NoSuchElementException("Entry with ID " + idEntry + " does not exist.");
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
            imageDataService.deleteImageData(getRecipeById(id).getPhoto_url());
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

    /**
     * Retrieves all recipes that belong to a specific contest.
     *
     * @param idContest The ID of the contest.
     * @return A list of recipes associated with the contest.
     */
    @Override
    public List<Recipe> getRecipeByIdContest(int idContest) {
         return recipeRepository.findRecipesByContestId(idContest);
    }

    /**
     * Retrieves paginated recipes created by a specific user.
     *
     * @param userMail the unique identifier of the user.
     * @param page     and size The pagination information.
     * @param size the size of the pagination
     * @return A paginated list of recipes.
     */
    @Override
    public Page<Recipe> getRecipeByUserMail(String userMail, String keyword, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return recipeRepository.findRecipesByUserMailAndKeyword(userMail, keyword, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recipes for user email " + userMail + ": " + e.getMessage(), e);
        }
    }


    /**
     * Sets the "masked" field of a recipe to true, effectively anonymizing it.
     *
     * @param recipeId The ID of the recipe to anonymize.
     */
    @Transactional
    public boolean anonymizeRecipe(int recipeId) {
        try {
            Recipe existingRecipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new NoSuchElementException("Recipe with ID " + recipeId + " not found"));
            recipeRepository.updateMasked(recipeId, true);
            return true;
        }catch (NoSuchElementException e) {
            return false;
        }

    }

}
