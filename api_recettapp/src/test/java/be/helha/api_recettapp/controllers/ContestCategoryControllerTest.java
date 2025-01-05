package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.controllers.ContestCategoryController;
import be.helha.api_recettapp.models.AppError;
import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.services.ContestCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for ContestCategoryController.
 */
public class ContestCategoryControllerTest {
    @InjectMocks
    private ContestCategoryController controller;
    @Mock
    private ContestCategoryService service;
    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Test for retrieving all categories.
     */
    @Test
    public void testGetAllCategories() {
        // Mock the service response
        List<ContestCategory> mockCategories = Arrays.asList(
                new ContestCategory(1L, "Dessert", "Sweet"),
                new ContestCategory(2L, "Starter", "Soup")
        );
        when(service.findAll()).thenReturn(mockCategories);
        // Call the controller method
        ResponseEntity<?> response = controller.getAllCategories();
        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        // Verify service interaction
        verify(service, times(1)).findAll();
    }
    /**
     * Test for retrieving a category by its ID.
     */
    @Test
    public void testGetCategoryById() {
        // Mock the service response
        ContestCategory mockCategory = new ContestCategory(1L, "Dessert", "Tiramisu");
        when(service.findById(1L)).thenReturn(Optional.of(mockCategory));
        // Call the controller method
        ResponseEntity<?> response = controller.getCategoryById(1L);
        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        ContestCategory body = (ContestCategory) response.getBody();
        assertNotNull(body);
        assertEquals("Dessert", body.getTitle());
        // Verify service interaction
        verify(service, times(1)).findById(1L);
    }
    /**
     * Test for retrieving a non-existent category by its ID.
     */
    @Test
    public void testGetCategoryById_NotFound() {
        // Mock the service response
        when(service.findById(999L)).thenReturn(Optional.empty());
        // Call the controller method
        ResponseEntity<?> response = controller.getCategoryById(999L);
        // Assertions
        assertEquals(404, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("ContestCategory with ID 999 not found.", error.getMessage());
        // Verify service interaction
        verify(service, times(1)).findById(999L);
    }
    /**
     * Test for creating a new category.
     */
    @Test
    public void testCreateCategory() {
        // Mock the service response
        ContestCategory mockCategory = new ContestCategory(3L, "Main Dish", "Pasta");
        when(service.save(any(ContestCategory.class))).thenReturn(mockCategory);
        // Call the controller method
        ContestCategory newCategory = new ContestCategory(null, "Main Dish", "Pasta");
        ResponseEntity<?> response = controller.createCategory(newCategory);
        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("ContestCategory created successfully with ID: 3", error.getMessage());
        // Verify service interaction
        verify(service, times(1)).save(newCategory);
    }
    /**
     * Test for updating an existing category.
     */
    @Test
    public void testUpdateCategory() {
        // Mock the service response
        ContestCategory existingCategory = new ContestCategory(1L, "Main Dish", "Pasta");
        when(service.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(service.save(any(ContestCategory.class))).thenReturn(existingCategory);
        // Call the controller method
        ContestCategory updatedCategory = new ContestCategory(null, "Main Dish Initial", "Pizza");
        ResponseEntity<?> response = controller.updateCategory(1L, updatedCategory);
        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("ContestCategory with ID 1 updated successfully.", error.getMessage());
        // Verify service interaction
        verify(service, times(1)).findById(1L);
        verify(service, times(1)).save(existingCategory);
    }
    /**
     * Test for deleting an existing category by its ID.
     */
    @Test
    public void testDeleteCategory() {
        // Mock the service response
        when(service.findById(1L)).thenReturn(Optional.of(new ContestCategory(1L, "Dessert", "Tiramisu")));
        // Call the controller method
        ResponseEntity<?> response = controller.deleteCategory(1L);
        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("ContestCategory with ID 1 deleted successfully.", error.getMessage());
        // Verify service interaction
        verify(service, times(1)).findById(1L);
        verify(service, times(1)).deleteById(1L);
    }
    /**
     * Test for deleting a non-existent category by its ID.
     */
    @Test
    public void testDeleteCategory_NotFound() {
        // Mock the service response
        when(service.findById(999L)).thenReturn(Optional.empty());
        // Call the controller method
        ResponseEntity<?> response = controller.deleteCategory(999L);
        // Assertions
        assertEquals(404, response.getStatusCodeValue());
        AppError error = (AppError) response.getBody();
        assertNotNull(error);
        assertEquals("ContestCategory with ID 999 not found. Deletion failed.", error.getMessage());
        // Verify service interaction
        verify(service, times(1)).findById(999L);
    }
}