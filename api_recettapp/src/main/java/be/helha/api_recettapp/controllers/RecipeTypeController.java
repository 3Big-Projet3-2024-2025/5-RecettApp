package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.services.RecipeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  Controller for managing RecipeType.
 * Provides endpoints for CRUD operations.
 */
@RestController
@RequestMapping("/api/recipe-types")
public class RecipeTypeController {

    @Autowired
    private RecipeTypeService service;

    /**
     * Get all recipe types.
     *
     * @return a list of all recipe types.
     */
    @GetMapping
    public ResponseEntity<List<RecipeType>> getAllRecipeTypes() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Get a specific recipe type by ID.
     *
     * @param id the ID of the recipe type.
     * @return the recipe type if found, or a 404 error if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeType> getRecipeTypeById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Find recipe types by name.
     *
     * @param name the name to search for.
     * @return a list of matching recipe types.
     */
    @GetMapping("/search")
    public ResponseEntity<List<RecipeType>> searchByName(@RequestParam String name) {
        List<RecipeType> recipeTypes = service.findByName(name);
        return ResponseEntity.ok(recipeTypes);
    }

    /**
     * Create a new recipe type.
     *
     * @param recipeType the recipe type to create.
     * @return the created recipe type.
     */
    @PostMapping
    public ResponseEntity<RecipeType> createRecipeType(@RequestBody RecipeType recipeType) {
        return ResponseEntity.ok(service.save(recipeType));
    }

    /**
     * Update an existing recipe type.
     *
     * @param id the ID of the recipe type to update.
     * @param updatedRecipeType the updated recipe type details.
     * @return the updated recipe type if it exists, or a 404 error if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeType> updateRecipeType(@PathVariable int id, @RequestBody RecipeType updatedRecipeType) {
        return service.findById(id)
                .map(existingRecipeType -> {
                    existingRecipeType.setName(updatedRecipeType.getName());
                    return ResponseEntity.ok(service.save(existingRecipeType));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a recipe type by ID.
     *
     * @param id the ID of the recipe type to delete.
     * @return a 204 status if the recipe type was deleted, or a 404 error if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeType(@PathVariable int id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
