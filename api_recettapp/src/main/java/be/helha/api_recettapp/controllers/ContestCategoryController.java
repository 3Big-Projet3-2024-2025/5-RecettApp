package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.AppError;
import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.services.ContestCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller to manage Contest Categories.
 * Provides endpoints for CRUD operations with descriptive messages.
 */
@RestController
@RequestMapping("/api/contest-categories")
public class ContestCategoryController {

    @Autowired
    private ContestCategoryService service;

    /**
     * Get all contest categories.
     *
     * @return a list of all contest categories.
     */
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<ContestCategory> categories = service.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.ok(new AppError("No ContestCategory found."));
        }
        return ResponseEntity.ok(categories);
    }

    /**
     * Find categories by title.
     *
     * @param title the title to search for.
     * @return a list of matching contest categories.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchByTitle(@RequestParam String title) {
        List<ContestCategory> categories = service.findByTitle(title);
        if (categories.isEmpty()) {
            return ResponseEntity.status(404).body(new AppError("No ContestCategory found with title: " + title));
        }
        return ResponseEntity.ok(categories);
    }

    /**
     * Get a specific contest category by ID.
     *
     * @param id the ID of the category.
     * @return the category if found, or a 404 error if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<ContestCategory> category = service.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        }
        return ResponseEntity.status(404).body(new AppError("ContestCategory with ID " + id + " not found."));
    }

    /**
     * Create a new contest category.
     *
     * @param category the category to create.
     * @return the created category.
     */
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody ContestCategory category) {
        try {
            ContestCategory savedCategory = service.save(category);
            return ResponseEntity.ok("ContestCategory created successfully with ID: " + savedCategory.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AppError("Failed to create ContestCategory: " + e.getMessage()));
        }
    }

    /**
     * Update an existing contest category.
     *
     * @param id the ID of the category to update.
     * @param updatedCategory the updated category details.
     * @return the updated category if it exists, or a 404 error if not found.
     */
    /**
     * Update an existing contest category.
     *
     * @param id the ID of the category to update.
     * @param updatedCategory the updated category details.
     * @return the updated category if it exists, or a 404 error if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody ContestCategory updatedCategory) {
        return service.findById(id)
                .map(existingCategory -> {
                    existingCategory.setTitle(updatedCategory.getTitle());
                    existingCategory.setDescription(updatedCategory.getDescription());
                    service.save(existingCategory);
                    return ResponseEntity.ok(new AppError("ContestCategory with ID " + id + " updated successfully."));
                })
                .orElse(ResponseEntity.status(404).body(new AppError("ContestCategory with ID " + id + " not found.")));
    }


    /**
     * Delete a contest category by ID.
     *
     * @param id the ID of the category to delete.
     * @return a 204 status if the category was deleted, or a 404 error if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok("ContestCategory with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(404).body(new AppError("ContestCategory with ID " + id + " not found. Deletion failed."));
        }
    }
}
