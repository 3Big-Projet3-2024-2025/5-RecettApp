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

}
