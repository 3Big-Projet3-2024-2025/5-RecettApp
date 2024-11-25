package be.helha.api_recettapp;

import be.helha.api_recettapp.models.Ingredient;
import be.helha.api_recettapp.services.IngredientServiceDB;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link IngredientServiceDB}, focusing on CRUD operations for {@link Ingredient}.
 * @author Demba Mohamed Samba
 * **/
@SpringBootTest
class IngredientTest {

    @Autowired
    private IngredientServiceDB ingredientService;

    /**
     * List to track ingredients during the tests.
     */
    public List<Ingredient> ingredientsList = new ArrayList<>();


    /**
     * Test case to add ingredients to the database and verify their addition.
     * <ul>
     *     <li>Creates two ingredients.</li>
     *     <li>Adds them to the database and the local list.</li>
     *     <li>Verifies the id is assigned and data matches expectations.</li>
     * </ul>
     * <p>
     *     This the first test
     * </p>
     */
    @Test
    @Order(1)
    void addIngredient() {
        // Create and add ingredientsList to the list
        Ingredient ingredient1 = new Ingredient(0, 101, 10, "Vegetables", "Carrot", "Root Vegetables",null);
        Ingredient ingredient2 = new Ingredient( 0,102, 11, "Fruits", "Apple", "Tree Fruits",null);

        // Add ingredientsList to the database
        Ingredient savedIngredient1 = ingredientService.addIngredient(ingredient1);
        Ingredient savedIngredient2 = ingredientService.addIngredient(ingredient2);

        // Add ingredientsList to the local list for tracking
        ingredientsList.add(savedIngredient1);
        ingredientsList.add(savedIngredient2);

        // Assertions
        assertNotNull(savedIngredient1.getId(), "First ingredient ID should not be null.");
        assertNotNull(savedIngredient2.getId(), "Second ingredient ID should not be null.");
        assertEquals("Carrot", savedIngredient1.getAlimentName(), "First ingredient name should be 'Carrot'.");
        assertEquals("Apple", savedIngredient2.getAlimentName(), "Second ingredient name should be 'Apple'.");
    }
    /**
     * Test case to retrieve ingredients by id and validate their data.
     * <ul>
     *     <li>Retrieves ingredients added in the previous test case.</li>
     *     <li>Compares the retrieved data with the local list.</li>
     * </ul>
     */
    @Test
    @Order(2)
    void getIngredient() {
        // Retrieve the first added ingredient
        while (ingredientsList.iterator().hasNext()) {
            
            Ingredient ingredient = ingredientsList.iterator().next();
            Ingredient retrievedIngredient = ingredientService.getIngredientById(ingredient.getId());

            // Compare it with the local list
            assertNotNull(retrievedIngredient, "Retrieved ingredient should not be null.");
            assertEquals(ingredientsList.get(0).getAlimentName(), retrievedIngredient.getAlimentName(), "Ingredient names should match.");
            assertEquals(ingredientsList.get(0).getAlimentGroupCode(), retrievedIngredient.getAlimentGroupCode(), "Ingredient group codes should match.");
            assertEquals(ingredientsList.get(0).getAlimentGroupName(), retrievedIngredient.getAlimentGroupName(), "Ingredient group names should match.");
        }
    }

    /**
     * Test case to update existing ingredients in the database.
     * <ul>
     *     <li>Updates the first ingredient in the list.</li>
     *     <li>Changes the name and group code.</li>
     *     <li>Verifies the changes in the database and updates the local list.</li>
     * </ul>
     */
    @Test
    @Order(3)
    void updateIngredient() {
        // Update the first ingredient in the list

        while (ingredientsList.iterator().hasNext()) {

            Ingredient ingredientToUpdate =ingredientsList.iterator().next();
            ingredientToUpdate.setAlimentName("Updated Carrot");
            ingredientToUpdate.setAlimentGroupCode(15); // Update group code

            Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientToUpdate);

            // Update the local list
            ingredientsList.set(0, updatedIngredient);

            // Assertions
            assertNotNull(updatedIngredient, "Updated ingredient should not be null.");
            assertEquals("Updated Carrot", updatedIngredient.getAlimentName(), "Updated aliment name should be 'Updated Carrot'.");
            assertEquals(15, updatedIngredient.getAlimentGroupCode(), "Updated aliment group code should be 15.");
        }
    }

    /**
     * Test case to delete ingredients from the database.
     * <ul>
     *     <li>Deletes all ingredients added in previous tests.</li>
     *     <li>Ensures they can no longer be retrieved from the database.</li>
     *     <li>Clears the local list after deletion.</li>
     * </ul>
     */
    @Test
    @Order(4)
    void deleteIngredient() {
        // Delete all the ingredientsList added before
        for (Ingredient ingredient : ingredientsList) {
            ingredientService.deleteIngredient(ingredient.getId());
            assertThrows(
                    RuntimeException.class,
                    () -> ingredientService.getIngredientById(ingredient.getId()),
                    "Ingredient should no longer exist in the database."
            );
        }

        // Clear the local list
        ingredientsList.clear();
    }

    /**
     * Test case to verify that retrieving a non-existent ingredient throws an exception.
     * <ul>
     *     <li>Attempts to get an ingredient with an ID that doesn't exist.</li>
     *     <li>Expects a {@link RuntimeException} to be thrown.</li>
     * </ul>
     */
    @Test
    @Order(5)
    void getNonExistentIngredients() {
        int nonExistentId = 99999; // an id that doesn't exist in the database.

        // Verify that attempting to get a non-existent ingredient throws an exception.
        assertThrows(
                RuntimeException.class,
                () -> ingredientService.getIngredientById(nonExistentId),
                "Retrieving a non-existent ingredient should throw a NoSuchElementException."
        );
    }

}
