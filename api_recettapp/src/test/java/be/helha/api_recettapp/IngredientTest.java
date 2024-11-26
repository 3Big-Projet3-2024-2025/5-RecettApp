package be.helha.api_recettapp;

import be.helha.api_recettapp.controllers.IngredientController;
import be.helha.api_recettapp.models.Ingredient;
import be.helha.api_recettapp.services.IIngredientService;
import be.helha.api_recettapp.services.IngredientServiceDB;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@SpringBootTest // Démarre tout le contexte Spring
@AutoConfigureMockMvc
class IngredientTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IIngredientService ingredientService;


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

    @Test
    public void testDeleteIngredient() throws Exception {
        doNothing().when(ingredientService).deleteIngredient(1);

        mockMvc.perform(delete("/ingredients/1"))
                .andExpect(status().isOk());

        verify(ingredientService, times(1)).deleteIngredient(1);
    }
}
