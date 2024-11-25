package be.helha.api_recettapp;

import be.helha.api_recettapp.models.Ingredient;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.models.RecipeComponent;
import be.helha.api_recettapp.services.IngredientServiceDB;
import be.helha.api_recettapp.services.RecipeComponentServiceDB;
import be.helha.api_recettapp.services.RecipeServiceDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RecipeComponentsTest {
    @Autowired
    private RecipeServiceDB recipeServiceDB;
    @Autowired
    private IngredientServiceDB ingredientServiceDB;
    @Autowired
    private RecipeComponentServiceDB recipeComponentServiceDB;
    @Test
    void addRecipeComponent() {

        Ingredient ingredient = ingredientServiceDB.getIngredientById(2); // ID existant dans la DB
        if (ingredient == null) {
            throw new IllegalStateException("Ingredient with ID 3196 not found.");
        }

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);

        Recipe recipe = recipeServiceDB.getRecipeById(4); // ID existant dans la DB
        if (recipe == null) {
            throw new IllegalStateException("Recipe with ID 1 not found.");
        }

        RecipeComponent recipeComponent = new RecipeComponent(0, recipe, 5, ingredient, "gram");
        RecipeComponent recipeComponent1 =recipeComponentServiceDB.addRecipeComponent(recipeComponent);


        RecipeComponent retrieved = recipeComponentServiceDB.getRecipeComponentById(recipeComponent1.getId());
        assertNotNull(retrieved);
        assertEquals(recipeComponent.getQuantity(), retrieved.getQuantity());

        System.out.println("RecipeComponent added successfully: " + retrieved);
    }
}
