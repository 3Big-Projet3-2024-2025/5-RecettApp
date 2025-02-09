package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.services.RecipeServiceDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Unit tests for {@link RecipeController}, focusing on CRUD operations for {@link Recipe} withe {@link MockMvc}.
 * @author Demba Mohamed Samba
 * **/
//@WebMvcTest(IngredientController.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@MockBean(RecipeServiceDB.class)
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeServiceDB recipeService;

    /**
     * Creates a new Recipe instance with the provided title, description, and category,
     * and sets default values for other fields.
     *
     * @param title       The title of the recipe.
     * @param description The description of the recipe.
     * @param category    The category of the recipe (e.g., Dessert, Main Course).
     * @return A new {@link Recipe}  with the provided and default values.
     */
    private Recipe createRecipe(String title,String description,String category) {
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setCategory(category);
        recipe.setPreparation_time(30);
        recipe.setCooking_time(45);
        recipe.setServings(8);
        recipe.setDifficulty_level("Medium");
        recipe.setInstructions("Mix ingredients, bake for 45 minutes.");


        Entry entry = new Entry();
        entry.setId(1);


        RecipeType recipeType = new RecipeType();
        recipeType.setId(2);
        recipe.setRecipe_type(recipeType);

        return recipe;
    }

    /**
     * Tests the addRecipe functionality of the RecipeController.
     *
     * This method verifies if a recipe can be successfully added via a POST request to the
     * "/recipe" endpoint.
     *
     * @throws Exception if any error occurs during the test execution.
     */
    @Test
    public void testAddRecipe()throws Exception{

        Recipe recipe = createRecipe("Chocolate Cake","Delicious chocolate cake recipe.","Dessert");

        given(recipeService.addRecipe(Mockito.any(Recipe.class))).willReturn(recipe);


        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Chocolate Cake"))
                .andExpect(jsonPath("$.category").value("Dessert"))
                .andExpect(jsonPath("$.preparation_time").value(30))
                .andExpect(jsonPath("$.cooking_time").value(45))
                .andExpect(jsonPath("$.servings").value(8))
                .andExpect(jsonPath("$.difficulty_level").value("Medium"));


    }

    /**
     * Tests the getRecipeById functionality of the RecipeController.
     *
     * This method verifies if a recipe can be successfully retrieved via a GET request to the "/recipe/1" endpoint.
     * @throws Exception if any error occurs during the test execution.
     */
    @Test
    public void testGetRecipeById() throws Exception {
        Recipe recipe = createRecipe("Chocolate Cake", "Delicious chocolate cake recipe.", "Dessert");
        recipe.setId(1);

        given(recipeService.getRecipeById(1)).willReturn(recipe);

        mockMvc.perform(get("/recipe/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Chocolate Cake"))
                .andExpect(jsonPath("$.category").value("Dessert"))
                .andExpect(jsonPath("$.preparation_time").value(30))
                .andExpect(jsonPath("$.cooking_time").value(45))
                .andExpect(jsonPath("$.servings").value(8))
                .andExpect(jsonPath("$.difficulty_level").value("Medium"));
    }

    /**
     * Tests the getAllRecipes functionality of the RecipeController.
     *
     * This method verifies if a list of recipes can be successfully retrieved
     * via a GET request to the "/recipe/all" endpoint.
     *
     * @throws Exception if any error occurs during the test execution.
     */
    @Test
    public void testGetAllRecipes() throws Exception {
        Recipe recipe1 = createRecipe("Chocolate Cake", "Delicious chocolate cake recipe.", "Dessert");
        recipe1.setId(1);

        Recipe recipe2 = createRecipe("Vanilla Ice Cream", "Delicious vanilla ice cream recipe.", "Dessert");
        recipe2.setId(2);

        given(recipeService.getRecipes()).willReturn(List.of(recipe1, recipe2));

        mockMvc.perform(get("/recipe/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Chocolate Cake"))
                .andExpect(jsonPath("$[1].title").value("Vanilla Ice Cream"));
    }

    /**
     * Tests the getRecipesWithPagination functionality of the RecipeController.
     *
     * This method verifies if a paginated list of recipes can be successfully retrieved
     * via a GET request to the "/recipe" endpoint with specific page and size parameters.
     *
     * @throws Exception if any error occurs during the test execution.
     */
    @Test
    public void testGetRecipesWithPagination() throws Exception {
        Recipe recipe1 = createRecipe("Chocolate Cake", "Delicious chocolate cake recipe.", "Dessert");
        recipe1.setId(1);

        Recipe recipe2 = createRecipe("Vanilla Ice Cream", "Delicious vanilla ice cream recipe.", "Dessert");
        recipe2.setId(2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> recipePage = new PageImpl<>(Arrays.asList(recipe1, recipe2), pageable, 2);

        // Mock du service
        when(recipeService.getRecipes(null, pageable)).thenReturn(recipePage);

        // Exécution et vérification
        mockMvc.perform(get("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Chocolate Cake"))
                .andExpect(jsonPath("$.content[1].title").value("Vanilla Ice Cream"));
    }



    /**
     * Tests the updateRecipe functionality of the RecipeController.
     *
     * This method verifies if a recipe can be successfully updated via a PUT request to the "/recipe" endpoint.
     *
     * @throws Exception if any error occurs during the test execution.
     */
    @Test
    public void testUpdateRecipe() throws Exception {
        Recipe updatedRecipe = createRecipe("Updated Cake", "Delicious chocolate cake recipe", "Dessert");
        updatedRecipe.setId(1);

        given(recipeService.updateRecipe(Mockito.any(Recipe.class))).willReturn(updatedRecipe);

        mockMvc.perform(put("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Cake"))
                .andExpect(jsonPath("$.category").value("Dessert"))
                .andExpect(jsonPath("$.description").value("Delicious chocolate cake recipe"));
    }

    /**
     * Tests the deleteRecipe functionality of the RecipeController.
     *
     * This method verifies if a recipe can be successfully deleted via a DELETE request to the
     * "/recipe/1" endpoint using the mockMvc framework.
     *
     * @throws Exception if any error occurs during the test execution.
     */
    @Test
    void testDeleteRecipe() throws Exception {
        Mockito.doNothing().when(recipeService).deleteRecipe(1);

        mockMvc.perform(delete("/recipe/1"))
                .andExpect(status().isOk());
    }
}
