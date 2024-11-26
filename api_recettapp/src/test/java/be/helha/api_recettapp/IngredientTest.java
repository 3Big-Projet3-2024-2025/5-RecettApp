package be.helha.api_recettapp;

import be.helha.api_recettapp.models.Ingredient;
import be.helha.api_recettapp.services.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Unit tests for {@link IngredientServiceDB}, focusing on CRUD operations for {@link Ingredient} withe {@link MockMvc}.
 * @author Demba Mohamed Samba
 * **/
//@WebMvcTest(IngredientController.class)
@SpringBootTest
@AutoConfigureMockMvc
class IngredientTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IIngredientService ingredientService;

    /**
     * Tests the getIngredients API endpoint by performing HTTP GET request
     * with specific pagination parameters and verifying the response.
     *
     * @throws Exception if there is an error during the test execution
     */
    @Test
    public void testGetIngredients() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient(1, 101, 201, "Fruits", "Apple", "Fresh Apple", null),
                new Ingredient(2, 102, 202, "Vegetables", "Carrot", "Fresh Carrot", null)
        );
        Page<Ingredient> ingredientPage = new PageImpl<>(ingredients, pageable, ingredients.size());

        when(ingredientService.getIngredients(pageable)).thenReturn(ingredientPage);

        mockMvc.perform(get("/ingredients")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].alimentGroupName", is("Fruits")))
                .andExpect(jsonPath("$.content[1].alimentName", is("Carrot")));
    }

    /**
     * Tests the `getAllIngredients` API endpoint by performing an HTTP GET request
     * and verifying that the response contains the expected list of ingredients.
     *
     * @throws Exception if there is an error during the test execution
     */
    @Test
    public void testGetAllIngredients() throws Exception {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient(1, 101, 201, "Fruits", "Apple", "Fresh Apple", null),
                new Ingredient(2, 102, 202, "Vegetables", "Carrot", "Fresh Carrot", null)
        );

        when(ingredientService.getIngredients()).thenReturn(ingredients);

        mockMvc.perform(get("/ingredients/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].alimentGroupName", is("Fruits")))
                .andExpect(jsonPath("$[1].alimentSpecifyGroupName", is("Fresh Carrot")));
    }

    /**
     * Tests the `getIngredientById` API endpoint by performing an HTTP GET request
     * to retrieve a specific ingredient by its ID and verifying the response.
     *
     * @throws Exception if there is an error during the test execution
     */
    @Test
    public void testGetIngredientById() throws Exception {
        Ingredient ingredient = new Ingredient(1, 101, 201, "Fruits", "Apple", "Fresh Apple", null);

        when(ingredientService.getIngredientById(1)).thenReturn(ingredient);

        mockMvc.perform(get("/ingredients/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alimentGroupName", is("Fruits")))
                .andExpect(jsonPath("$.alimentName", is("Apple")));
    }

    /**
     * Tests the `addIngredient` API endpoint by performing an HTTP POST request to add a new ingredient
     * and verifying that the response contains the expected ingredient details.
     *
     * @throws Exception if there is an error during the test execution
     */
    @Test
    public void testAddIngredient() throws Exception {
        Ingredient newIngredient = new Ingredient(3, 103, 203, "Meat", "Chicken", "Fresh Chicken", null);

        when(ingredientService.addIngredient(Mockito.any())).thenReturn(newIngredient);

        ResultActions reponse = mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"alimentSpecifyGroupCode\":103,\"alimentGroupCode\":203,\"alimentGroupName\":\"Meat\",\"alimentName\":\"Chicken\",\"alimentSpecifyGroupName\":\"Fresh Chicken\"}"));
        reponse.andExpect(status().isOk())
                .andExpect(jsonPath("$.alimentGroupName", is("Meat")))
                .andExpect(jsonPath("$.alimentName", is("Chicken")));
    }

    /**
     * Tests the `updateIngredient` API endpoint by performing an HTTP PUT request
     * to update an existing ingredient and verifies that the response contains the
     * expected updated ingredient details.
     *
     * @throws Exception if there is an error during the test execution
     */
    @Test
    public void testUpdateIngredient() throws Exception {
        Ingredient updatedIngredient = new Ingredient(1, 101, 201, "Fruits", "Updated Apple", "Fresh Apple", null);

        when(ingredientService.updateIngredient(Mockito.any())).thenReturn(updatedIngredient);

        mockMvc.perform(put("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"alimentSpecifyGroupCode\":101,\"alimentGroupCode\":201,\"alimentGroupName\":\"Fruits\",\"alimentName\":\"Updated Apple\",\"alimentSpecifyGroupName\":\"Fresh Apple\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alimentName", is("Updated Apple")));
    }

    /**
     * Performs a test to verify the deletion of an ingredient via the delete API endpoint.
     *
     * This test performs an HTTP DELETE request to the
     * /ingredients/1 endpoint and expects a 200 OK status and verifies that the
     * `deleteIngredient` method was called exactly once with the specified ID.
     *
     * @throws Exception if there is an error during the test execution.
     */
    @Test
    public void testDeleteIngredient() throws Exception {
        doNothing().when(ingredientService).deleteIngredient(1);

        mockMvc.perform(delete("/ingredients/1"))
                .andExpect(status().isOk());

        verify(ingredientService, times(1)).deleteIngredient(1);
    }
}
