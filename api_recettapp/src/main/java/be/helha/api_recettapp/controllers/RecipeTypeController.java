package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.services.RecipeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
     * @return a list of all recipe types or a message if none exist.
     */
    @GetMapping
    public ResponseEntity<?> getAllRecipeTypes() {
        List<RecipeType> recipeTypes = service.findAll();
        if (recipeTypes.isEmpty()) {
            return ResponseEntity.ok("No recipe types found.");
        }
        return ResponseEntity.ok(recipeTypes);
    }

    /**
     * Get a specific recipe type by ID.
     *
     * @param id the ID of the recipe type.
     * @return the recipe type if found, or a 404 error if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRecipeTypeById(@PathVariable int id) {
        // Check if the RecipeType exists
        Optional<RecipeType> recipeType = service.findById(id);
        return recipeType.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body("Recipe type with ID " + id + " not found."));
    }


    /**
     * Find recipe types by name.
     *
     * @param name the name to search for.
     * @return a list of matching recipe types or a message if none match.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        List<RecipeType> recipeTypes = service.findByName(name);
        if (recipeTypes.isEmpty()) {
            return ResponseEntity.status(404).body("No recipe types found with name: " + name);
        }
        return ResponseEntity.ok(recipeTypes);
    }

    /**
     * Create a new recipe type.
     *
     * @param recipeType the recipe type to create.
     * @return a message indicating the creation was successful.
     */
    @PostMapping
    public ResponseEntity<?> createRecipeType(@RequestBody RecipeType recipeType) {
        try {
            RecipeType savedRecipeType = service.save(recipeType);
            return ResponseEntity.ok("Recipe type created successfully with ID: " + savedRecipeType.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create recipe type: " + e.getMessage());
        }
    }

    /**
     * Update an existing recipe type.
     *
     * @param id the ID of the recipe type to update.
     * @param updatedRecipeType the updated recipe type details.
     * @return the updated recipe type if it exists, or a 404 error if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipeType(@PathVariable int id, @RequestBody RecipeType updatedRecipeType) {
        return service.findById(id)
                .map(existingRecipeType -> {
                    existingRecipeType.setName(updatedRecipeType.getName());
                    service.save(existingRecipeType);
                    return ResponseEntity.ok("Recipe type with ID " + id + " updated successfully.");
                })
                .orElse(ResponseEntity.status(404).body("Recipe type with ID " + id + " not found."));
    }

    /**
     * Delete a recipe type by ID.
     *
     * @param id the ID of the recipe type to delete.
     * @return a success message if the recipe type was deleted, or a 404 error if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipeType(@PathVariable int id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok("Recipe type with ID " + id + " deleted successfully.");
        }
        return ResponseEntity.status(404).body("Recipe type with ID " + id + " not found. Deletion failed.");
    }
}
