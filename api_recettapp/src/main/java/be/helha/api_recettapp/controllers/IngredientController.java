package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Ingredient;
import be.helha.api_recettapp.services.IIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


/**
 * REST controller for managing ingredients.
 * Provides endpoints for retrieving, adding, updating, and deleting ingredients.
 *
 * <p>
 * The controller is mapped to the path "/ingredients".
 * It delegates operations to the {@link IIngredientService}.
 * </p>
 */
@RestController
@RequestMapping(path = "/ingredients")
public class IngredientController {

    @Autowired
    private IIngredientService ingredientService;

    /**
     * Retrieves a paginated list of ingredients.
     *
     * @param pageable the pagination information.
     * @return a {@link Page} of {@link Ingredient}.
     */
    @GetMapping
    public Page<Ingredient> getIngredients(Pageable pageable) {
        return ingredientService.getIngredients(pageable);
    }

    /**
     * Retrieves a list of all ingredients without pagination.
     *
     * @return a {@link List} of {@link Ingredient}.
     */
    @GetMapping("/all")
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getIngredients();
    }

    /**
     * Retrieves an ingredient by its ID.
     *
     * @param id the ID of the ingredient.
     * @return the requested {@link Ingredient}.
     * @throws NoSuchElementException if the ingredient with the given ID is not found.
     */
    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable int id) {
        return ingredientService.getIngredientById(id);
    }

    /**
     * Adds a new ingredient.
     *
     * @param ingredient the ingredient to add.
     * @return the added {@link Ingredient}.
     */
    @PostMapping
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.addIngredient(ingredient);
    }

    /**
     * Updates an existing ingredient by its ID.
     *
     * @param ingredient the updated ingredient data.
     * @return the updated {@link Ingredient}.
     * @throws NoSuchElementException if the ingredient with the given ID is not found.
     */
    @PutMapping
    public Ingredient updateIngredient(@RequestBody Ingredient ingredient){
        return ingredientService.updateIngredient(ingredient);
    }

    /**
     * Deletes an ingredient by its ID.
     *
     * @param id the ID of the ingredient to delete.
     * @throws NoSuchElementException if the ingredient with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable int id) {
        ingredientService.deleteIngredient(id);
    }
}

