package be.helha.api_recettapp;

import be.helha.api_recettapp.models.*;
import be.helha.api_recettapp.services.IRecipeComponent;
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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    /**
     * Tests the retrieval of a RecipeComponent by its ID.
     *
     * This method ensures that a GET request to the "/recipe-components/1" endpoint
     * returns the correct RecipeComponent with the appropriate details.
     *
     * @throws Exception if any error occurs during the execution of the test.
     */
    @Test
    public void testGetRecipeComponentById() throws Exception {
        RecipeComponent recipeComponent = createRecipeComponent(1, 200, "g");

        given(recipeComponentService.getRecipeComponentById(1)).willReturn(recipeComponent);

        mockMvc.perform(get("/recipe-components/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(200))
                .andExpect(jsonPath("$.unit").value("g"))
                .andExpect(jsonPath("$.recipe.id").value(1))
                .andExpect(jsonPath("$.ingredient.id").value(1));
    }

    /**
     * Tests the retrieval of all RecipeComponent entities in the system.
     *
     * This method sends a GET request to the "/recipe-components/all" endpoint
     * and verifies the response. The expected response should be a list of
     * two RecipeComponents with the specified quantities and units.
     *
     * @throws Exception if any error occurs during the execution of the test.
     */
    @Test
    public void testGetAllRecipeComponents() throws Exception {
        RecipeComponent component1 = createRecipeComponent(1, 200, "g");
        RecipeComponent component2 = createRecipeComponent(2, 100, "ml");

        given(recipeComponentService.getRecipeComponents()).willReturn(List.of(component1, component2));

        mockMvc.perform(get("/recipe-components/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].quantity").value(200))
                .andExpect(jsonPath("$[0].unit").value("g"))
                .andExpect(jsonPath("$[1].quantity").value(100))
                .andExpect(jsonPath("$[1].unit").value("ml"));
    }
}
