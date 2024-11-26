package be.helha.api_recettapp;

import be.helha.api_recettapp.controllers.RecipeController;
import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.Recipe;
import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.services.IRecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Unit tests for {@link RecipeController}, focusing on CRUD operations for {@link Recipe} withe {@link MockMvc}.
 * @author Demba Mohamed Samba
 * **/
//@WebMvcTest(IngredientController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRecipeService recipeService;

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
        recipe.setApproved(true);

        Contest contest = new Contest();
        contest.setId(1);
        recipe.setContest(contest);

        RecipeType recipeType = new RecipeType();
        recipeType.setId(2);
        recipe.setRecipe_type(recipeType);

        return recipe;
    }

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
                .andExpect(jsonPath("$.difficulty_level").value("Medium"))
                .andExpect(jsonPath("$.approved").value(true));


    }

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
                .andExpect(jsonPath("$.difficulty_level").value("Medium"))
                .andExpect(jsonPath("$.approved").value(true));
    }

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
}
