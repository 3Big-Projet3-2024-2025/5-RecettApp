package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.services.IRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController {

    @Autowired
    private IRecipeService recipeService;

    /**
     * Retrieves a paginated list of recipes.
     *
     * @param pageable the pagination information.
     * @return a {@link Page} of {@link Recipe}.
     */
    @GetMapping
    public Page<Recipe> getRecipes(Pageable pageable) {
        return recipeService.getRecipes(pageable);
    }

    /**
     * Retrieves a list of all recipes without pagination.
     *
     * @return a {@link List} of {@link Recipe}.
     */
    @GetMapping("/all")
    public List<Recipe> getAllRecipes() {
        return recipeService.getRecipes();
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe.
     * @return the requested {@link Recipe}.
     * @throws NoSuchElementException if the recipe with the given ID is not found.
     */
    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable int id) {
        return recipeService.getRecipeById(id);
    }
    /**
     * Retrieves a recipe by its ID.
     *
     * @param idContest the ID of the contest.
     * @return a {@link List} of {@link Recipe}.
     * @throws NoSuchElementException if the recipe with the given ID is not found.
     */
    @GetMapping("contest/{idContest}")
    public List<Recipe> getRecipeByIdContest(@PathVariable int idContest) {
        return recipeService.getRecipeByIdContest(idContest);
    }

    /**
     * Adds a new recipe.
     *
     * @param recipe the recipe to add.
     * @return the added {@link Recipe}.
     */
    @PostMapping
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        recipe.setId(0);
        recipe.setMasked(false);
        return recipeService.addRecipe(recipe);
    }

    /**
     * Updates an existing recipe by its ID.
     *
     * @param recipe the updated recipe data.
     * @return the updated {@link Recipe}.
     * @throws NoSuchElementException if the recipe with the given ID is not found.
     */
    @PutMapping
    public Recipe updateRecipe(@RequestBody Recipe recipe){
        return recipeService.updateRecipe(recipe);
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to delete.
     * @throws NoSuchElementException if the recipe with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable int id) {
        recipeService.deleteRecipe(id);
    }

    /**
     * Retrieves paginated recipes created by a specific user.
     *
     * @param email The email address of the user.
     * @param page and size The pagination information.
     * @return A paginated list of recipes.
     */
    @GetMapping("/usermail")
    public Page<Recipe> getRecipesByUserMailWithPagination(@RequestParam String email,@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return recipeService.getRecipeByUserMail(email, page, size);
        } catch (Exception e) {
            throw new NoSuchElementException("This user has no recipes" + e.getMessage());
        }
    }


    /**
     * Sets the "masked" field of a recipe to true, effectively anonymizing it.
     *
     * @param id The ID of the recipe to anonymize.
     * @return a boolean
     */
    @PutMapping("/anonymize/{id}")
    public boolean anonymizeRecipe(@PathVariable int id) {
        try {
            return recipeService.anonymizeRecipe(id);
        } catch (Exception e) {
            throw new RuntimeException(" can't anonymize the recipe :" + e.getMessage());
        }
    }
}
