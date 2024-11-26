package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.services.ContestCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  Controller to manage Contest Categories.
 * It provides endpoints for CRUD operations.
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
    public ResponseEntity<List<ContestCategory>> getAllCategories() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Find categories by title.
     *
     * @param title the title to search for.
     * @return a list of matching contest categories.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ContestCategory>> searchByTitle(@RequestParam String title) {
        List<ContestCategory> categories = service.findByTitle(title);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get a specific contest category by ID.
     *
     * @param id the ID of the category.
     * @return the category if found, or a 404 error if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContestCategory> getCategoryById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new contest category.
     *
     * @param category the category to create.
     * @return the created category.
     */
    @PostMapping
    public ResponseEntity<ContestCategory> createCategory(@RequestBody ContestCategory category) {
        return ResponseEntity.ok(service.save(category));
    }

    /**
     * Update an existing contest category.
     *
     * @param id the ID of the category to update.
     * @param updatedCategory the updated category details.
     * @return the updated category if it exists, or a 404 error if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContestCategory> updateCategory(@PathVariable Long id, @RequestBody ContestCategory updatedCategory) {
        return service.findById(id)
                .map(existingCategory -> {
                    existingCategory.setTitle(updatedCategory.getTitle());
                    existingCategory.setDescription(updatedCategory.getDescription());
                    existingCategory.setStatus(updatedCategory.isStatus());
                    return ResponseEntity.ok(service.save(existingCategory));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a contest category by ID.
     *
     * @param id the ID of the category to delete.
     * @return a 204 status if the category was deleted, or a 404 error if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
