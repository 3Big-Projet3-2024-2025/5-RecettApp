package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.RecipeComponent;
import be.helha.api_recettapp.services.IRecipeComponent;
import be.helha.api_recettapp.services.IRecipeService;
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

    @Autowired
    private IRecipeService recipeService;

    /**
     * Retrieves a paginated list of recipe components.
     *
     * @param pageable the pagination information.
     * @return a {@link Page} of {@link RecipeComponent}.
     */
    @GetMapping
    public Page<RecipeComponent> getRecipeComponents(Pageable pageable) {
        try {
            return recipeComponentService.getRecipeComponents(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch recipe components: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of all recipe components without pagination.
     *
     * @return a {@link List} of {@link RecipeComponent}.
     */
    @GetMapping("/all")
    public List<RecipeComponent> getAllRecipeComponents() {
        try {
            return recipeComponentService.getRecipeComponents();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all recipe components: " + e.getMessage());
        }
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
        try {
            return recipeComponentService.getRecipeComponentById(id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Recipe component with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch recipe component by ID: " + e.getMessage());
        }
    }

    /**
     * Adds a new recipe component.
     *
     * @param recipeComponent the recipe component to add.
     * @return the added {@link RecipeComponent}.
     */
    @PostMapping
    public RecipeComponent addRecipeComponent(@RequestBody RecipeComponent recipeComponent) {
        try {
            recipeComponent.setId(0);
            return recipeComponentService.addRecipeComponent(recipeComponent);
        } catch (Exception e) {
            recipeService.deleteRecipe(recipeComponent.getRecipe().getId());
            throw new RuntimeException("Failed to add recipe component: " + e.getMessage());
        }
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
        try {
            return recipeComponentService.updateRecipeComponent(recipeComponent);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Recipe component with ID " + recipeComponent.getId() + " not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update recipe component: " + e.getMessage());
        }
    }

    /**
     * Deletes a recipe component by its ID.
     *
     * @param id the ID of the recipe component to delete.
     * @throws NoSuchElementException if the recipe component with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    public void deleteRecipeComponent(@PathVariable int id) {
        try {
            recipeComponentService.deleteRecipeComponent(id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Recipe component with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete recipe component: " + e.getMessage());
        }
    }

    /**
     * Find a recipe component by his recipe id
     *
     * @param id the id of the recipe
     * @return list a list of RecipeComponent objects
     */
    @GetMapping("/recipe/{id}")
    public List<RecipeComponent> findRecipeComponentsByRecipeId(@PathVariable int id) {
        try {
            return recipeComponentService.findRecipeComponentsByRecipeId(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch recipe components for recipe ID " + id + ": " + e.getMessage());
        }
    }
}
