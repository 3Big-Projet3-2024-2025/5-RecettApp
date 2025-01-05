package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.controllers.RecipeTypeController;
import be.helha.api_recettapp.models.AppError;
import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.services.RecipeTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Test class for RecipeTypeController.
 * This class tests all the methods in the RecipeTypeController.
 */
class RecipeTypeControllerTest {
    @InjectMocks
    private RecipeTypeController recipeTypeController;
    @Mock
    private RecipeTypeService recipeTypeService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Test for getAllRecipeTypes.
     * Verifies that it returns a list of recipe types when they exist.
     */
    @Test
    void getAllRecipeTypes_ShouldReturnListOfRecipeTypes() {
        // Arrange
        List<RecipeType> mockRecipeTypes = new ArrayList<>();
        mockRecipeTypes.add(new RecipeType() {{
            setId(1);
            setName("Main Dish");
        }});
        mockRecipeTypes.add(new RecipeType() {{
            setId(2);
            setName("Dessert");
        }});
        when(recipeTypeService.findAll()).thenReturn(mockRecipeTypes);
        // Act
        ResponseEntity<?> response = recipeTypeController.getAllRecipeTypes();
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockRecipeTypes, response.getBody());
        verify(recipeTypeService, times(1)).findAll();
    }
    /**
     * Test for getAllRecipeTypes.
     * Verifies that it returns a message if no recipe types are found.
     */
    @Test
    void getAllRecipeTypes_ShouldReturnMessageWhenNoRecipeTypesFound() {
        // Arrange
        when(recipeTypeService.findAll()).thenReturn(new ArrayList<>());
        // Act
        ResponseEntity<?> response = recipeTypeController.getAllRecipeTypes();
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("No recipe types found.", error.getMessage());
        verify(recipeTypeService, times(1)).findAll();
    }
    /**
     * Test for getRecipeTypeById.
     * Verifies that it returns a recipe type when the ID exists.
     */
    @Test
    void getRecipeTypeById_ShouldReturnRecipeTypeWhenFound() {
        // Arrange
        RecipeType mockRecipeType = new RecipeType();
        mockRecipeType.setId(1);
        mockRecipeType.setName("Main Dish");
        when(recipeTypeService.findById(1)).thenReturn(Optional.of(mockRecipeType));
        // Act
        ResponseEntity<Object> response = recipeTypeController.getRecipeTypeById(1);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockRecipeType, response.getBody());
        verify(recipeTypeService, times(1)).findById(1);
    }
    /**
     * Test for getRecipeTypeById.
     * Verifies that it returns an error message if the ID does not exist.
     */
    @Test
    void getRecipeTypeById_ShouldReturnErrorMessageWhenNotFound() {
        // Arrange
        when(recipeTypeService.findById(1)).thenReturn(Optional.empty());
        // Act
        ResponseEntity<Object> response = recipeTypeController.getRecipeTypeById(1);
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("Recipe type with ID 1 not found.", error.getMessage());
        verify(recipeTypeService, times(1)).findById(1);
    }
    /**
     * Test for createRecipeType.
     * Verifies that it returns a success message after creating a recipe type.
     */
    @Test
    void createRecipeType_ShouldReturnSuccessMessage() {
        // Arrange
        RecipeType mockRecipeType = new RecipeType();
        mockRecipeType.setId(1);
        mockRecipeType.setName("Main Dish");
        when(recipeTypeService.save(any(RecipeType.class))).thenReturn(mockRecipeType);
        // Act
        ResponseEntity<?> response = recipeTypeController.createRecipeType(mockRecipeType);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("Recipe type created successfully with ID: 1", error.getMessage());
        verify(recipeTypeService, times(1)).save(mockRecipeType);
    }
    /**
     * Test for updateRecipeType.
     * Verifies that it returns a success message if the recipe type is updated.
     */
    @Test
    void updateRecipeType_ShouldReturnSuccessMessageWhenFound() {
        // Arrange
        RecipeType mockRecipeType = new RecipeType();
        mockRecipeType.setId(1);
        mockRecipeType.setName("Starter");
        when(recipeTypeService.findById(1)).thenReturn(Optional.of(new RecipeType()));
        when(recipeTypeService.save(any(RecipeType.class))).thenReturn(mockRecipeType);
        // Act
        ResponseEntity<?> response = recipeTypeController.updateRecipeType(1, mockRecipeType);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("Recipe type with ID 1 updated successfully.", error.getMessage());
        verify(recipeTypeService, times(1)).findById(1);
        verify(recipeTypeService, times(1)).save(any(RecipeType.class));
    }
    /**
     * Test for updateRecipeType.
     * Verifies that it returns an error message if the ID does not exist.
     */
    @Test
    void updateRecipeType_ShouldReturnErrorMessageWhenNotFound() {
        // Arrange
        RecipeType mockRecipeType = new RecipeType();
        mockRecipeType.setId(1);
        mockRecipeType.setName("Starter");
        when(recipeTypeService.findById(1)).thenReturn(Optional.empty());
        // Act
        ResponseEntity<?> response = recipeTypeController.updateRecipeType(1, mockRecipeType);
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("Recipe type with ID 1 not found.", error.getMessage());
        verify(recipeTypeService, times(1)).findById(1);
        verify(recipeTypeService, times(0)).save(any(RecipeType.class));
    }
    /**
     * Test for deleteRecipeType.
     * Verifies that it returns a success message if the recipe type is deleted.
     */
    @Test
    void deleteRecipeType_ShouldReturnSuccessMessageWhenFound() {
        // Arrange
        when(recipeTypeService.findById(1)).thenReturn(Optional.of(new RecipeType()));
        // Act
        ResponseEntity<?> response = recipeTypeController.deleteRecipeType(1);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("Recipe type with ID 1 deleted successfully.", error.getMessage());
        verify(recipeTypeService, times(1)).findById(1);
        verify(recipeTypeService, times(1)).deleteById(1);
    }
    /**
     * Test for deleteRecipeType.
     * Verifies that it returns an error message if the ID does not exist.
     */
    @Test
    void deleteRecipeType_ShouldReturnErrorMessageWhenNotFound() {
        // Arrange
        when(recipeTypeService.findById(1)).thenReturn(Optional.empty());
        // Act
        ResponseEntity<?> response = recipeTypeController.deleteRecipeType(1);
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("Recipe type with ID 1 not found. Deletion failed.", error.getMessage());
        verify(recipeTypeService, times(1)).findById(1);
        verify(recipeTypeService, times(0)).deleteById(1);
    }
}