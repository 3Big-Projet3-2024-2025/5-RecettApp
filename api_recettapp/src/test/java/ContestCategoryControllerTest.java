import be.helha.api_recettapp.controllers.ContestCategoryController;
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
     * Test for retrieving all contest categories.
     */
    @Test
    public void testGetAllCategories() {
        // Mock service response
        List<ContestCategory> mockCategories = Arrays.asList(
                new ContestCategory(1L, "Dessert", "Sucré"),
                new ContestCategory(2L, "Entrée", "Soupe")
        );
        when(service.findAll()).thenReturn(mockCategories);

        // Call the controller method
        ResponseEntity<?> response = controller.getAllCategories();

        // Assert response
        assertEquals(200, response.getStatusCodeValue());
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());

        // Verify interaction with service
        verify(service, times(1)).findAll();
    }

    /**
     * Test for retrieving a single category by ID.
     */
    @Test
    public void testGetCategoryById() {
        // Mock service response
        ContestCategory mockCategory = new ContestCategory(1L, "Dessert", "Tiramissu");
        when(service.findById(1L)).thenReturn(Optional.of(mockCategory));

        // Call the controller method
        ResponseEntity<?> response = controller.getCategoryById(1L);

        // Assert response
        assertEquals(200, response.getStatusCodeValue());
        ContestCategory body = (ContestCategory) response.getBody();
        assertNotNull(body);
        assertEquals("Dessert", body.getTitle());

        // Verify interaction with service
        verify(service, times(1)).findById(1L);
    }

    /**
     * Test for retrieving a non-existent category by ID.
     */
    @Test
    public void testGetCategoryById_NotFound() {
        // Mock service response
        when(service.findById(999L)).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<?> response = controller.getCategoryById(999L);

        // Assert response
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("ContestCategory with ID 999 not found.", response.getBody());

        // Verify interaction with service
        verify(service, times(1)).findById(999L);
    }

    /**
     * Test for creating a new contest category.
     */
    @Test
    public void testCreateCategory() {
        // Mock service response
        ContestCategory mockCategory = new ContestCategory(3L, "Plat", "Pattes");
        when(service.save(any(ContestCategory.class))).thenReturn(mockCategory);

        // Call the controller method
        ContestCategory newCategory = new ContestCategory(null, "Plat", "Pattes");
        ResponseEntity<?> response = controller.createCategory(newCategory);

        // Assert response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ContestCategory created successfully with ID: 3", response.getBody());

        // Verify interaction with service
        verify(service, times(1)).save(newCategory);
    }

    /**
     * Test for updating an existing contest category.
     */
    @Test
    public void testUpdateCategory() {
        // Mock service response
        ContestCategory existingCategory = new ContestCategory(1L, "Plat", "Pattes");
        when(service.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(service.save(any(ContestCategory.class))).thenReturn(existingCategory);

        // Call the controller method
        ContestCategory updatedCategory = new ContestCategory(null, "Plat Initial", "Pizza");
        ResponseEntity<?> response = controller.updateCategory(1L, updatedCategory);

        // Assert response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ContestCategory with ID 1 updated successfully.", response.getBody());

        // Verify interaction with service
        verify(service, times(1)).findById(1L);
        verify(service, times(1)).save(existingCategory);
    }

    /**
     * Test for deleting an existing contest category by ID.
     */
    @Test
    public void testDeleteCategory() {
        // Mock service response
        when(service.findById(1L)).thenReturn(Optional.of(new ContestCategory(1L, "Dessert", "Tiramissu")));

        // Call the controller method
        ResponseEntity<?> response = controller.deleteCategory(1L);

        // Assert response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ContestCategory with ID 1 deleted successfully.", response.getBody());

        // Verify interaction with service
        verify(service, times(1)).findById(1L);
        verify(service, times(1)).deleteById(1L);
    }

    /**
     * Test for deleting a non-existent contest category by ID.
     */
    @Test
    public void testDeleteCategory_NotFound() {
        // Mock service response
        when(service.findById(999L)).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<?> response = controller.deleteCategory(999L);

        // Assert response
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("ContestCategory with ID 999 not found. Deletion failed.", response.getBody());

        // Verify interaction with service
        verify(service, times(1)).findById(999L);
    }
}
