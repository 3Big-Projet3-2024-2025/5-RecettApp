package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.RecipeComponent;
import be.helha.api_recettapp.services.IRecipeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST controller for managing recipe components.
 */
@RestController
@RequestMapping(path = "/recipe-components")
public class RecipeComponentController {


    @Autowired
    private IRecipeComponent recipeComponentService;

    /**
     * Retrieves a paginated list of recipe components.
     *
     * @param pageable the pagination information.
     * @return a {@link Page} of {@link RecipeComponent}.
     */
    @GetMapping
    public Page<RecipeComponent> getRecipeComponents(Pageable pageable) {
        return recipeComponentService.getRecipeComponents(pageable);
    }

    /**
     * Retrieves a list of all recipe components without pagination.
     *
     * @return a {@link List} of {@link RecipeComponent}.
     */
    @GetMapping("/all")
    public List<RecipeComponent> getAllRecipeComponents() {
        return recipeComponentService.getRecipeComponents();
    }

    /**
     * Retrieves a recipe component by its ID.
     *
     * @param id the ID of the recipe component.
     * @return the requested {@link RecipeComponent}.
     * @throws NoSuchElementException if the recipe component with the given ID is not found.
     */
    @GetMapping("/{id}")
    public RecipeComponent getRecipeComponentById(@PathVariable int id) {
        return recipeComponentService.getRecipeComponentById(id);
    }

    /**
     * Adds a new recipe component.
     *
     * @param recipeComponent the recipe component to add.
     * @return the added {@link RecipeComponent}.
     */
    @PostMapping
    public RecipeComponent addRecipeComponent(@RequestBody RecipeComponent recipeComponent) {
        recipeComponent.setId(0);
        return recipeComponentService.addRecipeComponent(recipeComponent);
    }

    /**
     * Updates an existing recipe component by its ID.
     *
     * @param recipeComponent the updated recipe component data.
     * @return the updated {@link RecipeComponent}.
     * @throws NoSuchElementException if the recipe component with the given ID is not found.
     */
    @PutMapping
    public RecipeComponent updateRecipeComponent(@RequestBody RecipeComponent recipeComponent) {
        return recipeComponentService.updateRecipeComponent(recipeComponent);
    }

    /**
     * Deletes a recipe component by its ID.
     *
     * @param id the ID of the recipe component to delete.
     * @throws NoSuchElementException if the recipe component with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    public void deleteRecipeComponent(@PathVariable int id) {
        recipeComponentService.deleteRecipeComponent(id);
    }

    @GetMapping("/recipe/{id}")
    public List<RecipeComponent> findRecipeComponentsByRecipeId(@PathVariable int id) {
        return recipeComponentService.findRecipeComponentsByRecipeId(id);
    }
}
