package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.repositories.jpa.RecipeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing RecipeType.
 * Contains the business logic for handling recipe types.
 */
@Service
public class RecipeTypeService {

    @Autowired
    private RecipeTypeRepository repository;

    /**
     * Retrieve all recipe types from the database.
     *
     * @return a list of all recipe types.
     */
    public List<RecipeType> findAll() {
        return repository.findAll();
    }

    /**
     * Find a specific recipe type by its ID.
     *
     * @param id the ID of the recipe type.
     * @return an Optional containing the recipe type if found, or empty if not.
     */
    public Optional<RecipeType> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Find recipe types by name.
     * This method retrieves all recipe types and filters them by the given name.
     *
     * @param name the name to search for.
     * @return a list of matching recipe types.
     */
    public List<RecipeType> findByName(String name) {
        return repository.findAll().stream()
                .filter(type -> type.getName() != null &&
                        type.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Save a new recipe type or update an existing one.
     *
     * @param recipeType the recipe type to save or update.
     * @return the saved recipe type.
     */
    public RecipeType save(RecipeType recipeType) {
        return repository.save(recipeType);
    }

    /**
     * Delete a recipe type by its ID.
     *
     * @param id the ID of the recipe type to delete.
     */
    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
