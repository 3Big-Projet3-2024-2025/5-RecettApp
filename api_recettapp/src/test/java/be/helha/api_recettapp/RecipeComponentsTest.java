package be.helha.api_recettapp;

import be.helha.api_recettapp.models.Ingredient;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.models.RecipeComponent;
import be.helha.api_recettapp.services.IRecipeComponent;
import be.helha.api_recettapp.services.IngredientServiceDB;
import be.helha.api_recettapp.services.RecipeComponentServiceDB;
import be.helha.api_recettapp.services.RecipeServiceDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeComponentsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRecipeComponent recipeComponentService;

    /**
     * Creates a RecipeComponent with the given id, quantity, and unit.
     * Initializes and sets associated Recipe and Ingredient objects.
     *
     * @param id the unique identifier for the RecipeComponent
     * @param quantity the quantity of the ingredient used in the recipe
     * @param unit the unit of measurement for the ingredient quantity
     * @return the {@link RecipeComponent }
     */
    private RecipeComponent createRecipeComponent(int id, int quantity, String unit) {
        RecipeComponent recipeComponent = new RecipeComponent();
        recipeComponent.setId(id);
        recipeComponent.setQuantity(quantity);
        recipeComponent.setUnit(unit);

        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setTitle("Chocolate Cake");
        recipeComponent.setRecipe(recipe);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setAlimentName("Sugar");
        recipeComponent.setIngredient(ingredient);

        return recipeComponent;
    }

    /**
     * Tests the functionality of adding a new recipe component.
     *This method verifies if a recipe-component can be successfully added via a POST request to the
     * "/recipe-components" endpoint.
     *
     * @throws Exception if any error occurs during the execution of the test.
     */
    @Test
    public void testAddRecipeComponent() throws Exception {
        RecipeComponent recipeComponent = createRecipeComponent(1, 200, "g");

        given(recipeComponentService.addRecipeComponent(Mockito.any(RecipeComponent.class))).willReturn(recipeComponent);

        mockMvc.perform(post("/recipe-components")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeComponent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(200))
                .andExpect(jsonPath("$.unit").value("g"))
                .andExpect(jsonPath("$.recipe.id").value(1))
                .andExpect(jsonPath("$.ingredient.id").value(1));
    }

}
