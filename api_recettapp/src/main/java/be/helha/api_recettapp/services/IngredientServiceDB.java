package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.Ingredient;
import be.helha.api_recettapp.repositories.jpa.IngredientRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Primary
public class IngredientServiceDB implements IIngredientService{

    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * Retrieves a paginated list of ingredients.
     *
     * @param page the {@link Pageable} object containing pagination information.
     * @return a {@link Page} of {@link Ingredient} objects.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public Page<Ingredient> getIngredients(Pageable page) {
        try {
            return ingredientRepository.findAll(page);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving ingredients: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of all ingredients.
     *
     * @return a {@link List} of {@link Ingredient} objects.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public List<Ingredient> getIngredients() {
        try {
            return ingredientRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving ingredients: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves an ingredient by its ID.
     *
     * @param id the ID of the {@link Ingredient} to retrieve.
     * @return the requested {@link Ingredient}.
     * @throws NoSuchElementException if the ingredient with the given ID does not exist.
     */
    @Override
    public Ingredient getIngredientById(int id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ingredient with ID " + id + " not found"));
    }

    /**
     * Adds a new ingredient to the system.
     *
     * @param ingredient the {@link Ingredient} object to add.
     * @return the added {@link Ingredient} object.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        try {
            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new RuntimeException("Error adding ingredient: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing ingredient in the system.
     *
     * @param ingredient the {@link Ingredient} object to update.
     * @return the updated {@link Ingredient} object.
     * @throws NoSuchElementException if the ingredient with the given ID does not exist.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public Ingredient updateIngredient(Ingredient ingredient) {
        if (!ingredientRepository.existsById(ingredient.getId())) {
            throw new NoSuchElementException("Ingredient with ID " + ingredient.getId() + " not found");
        }
        try {
            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new RuntimeException("Error updating ingredient: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an ingredient by its ID.
     *
     * @param id the ID of the {@link Ingredient} to delete.
     * @throws NoSuchElementException if the ingredient with the given ID does not exist.
     * @throws RuntimeException if any error occurs during the operation.
     */
    @Override
    public void deleteIngredient(int id) {
        if (!ingredientRepository.existsById(id)) {
            throw new NoSuchElementException("Ingredient with ID " + id + " not found");
        }
        try {
            ingredientRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting ingredient: " + e.getMessage(), e);
        }
    }



    }
