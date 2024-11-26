import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.repositories.jpa.RecipeTypeRepository;
import be.helha.api_recettapp.services.RecipeTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecipeTypeService.
 * This class tests the methods in the RecipeTypeService class
 * using Mockito to mock the repository layer.
 */
class RecipeTypeServiceTest {

    /**
     * Mocked instance of RecipeTypeRepository.
     */
    @Mock
    private RecipeTypeRepository repository;

    /**
     * Service being tested, with the repository mocked.
     */
    @InjectMocks
    private RecipeTypeService service;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the findAll method in the service.
     * Verifies that the method retrieves all recipe types.
     */
    @Test
    void testFindAll() {
        RecipeType type1 = new RecipeType();
        type1.setId(1);
        type1.setName("Appetizer");

        RecipeType type2 = new RecipeType();
        type2.setId(2);
        type2.setName("Dessert");

        when(repository.findAll()).thenReturn(Arrays.asList(type1, type2));

        List<RecipeType> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    /**
     * Tests the findById method in the service when the recipe type exists.
     * Verifies that the correct recipe type is returned.
     */
    @Test
    void testFindById() {
        RecipeType type = new RecipeType();
        type.setId(1);
        type.setName("Appetizer");

        when(repository.findById(1)).thenReturn(Optional.of(type));

        Optional<RecipeType> result = service.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Appetizer", result.get().getName());
        verify(repository, times(1)).findById(1);
    }

    /**
     * Tests the save method in the service.
     * Verifies that a recipe type is correctly saved.
     */
    @Test
    void testSave() {
        RecipeType type = new RecipeType();
        type.setName("Appetizer");

        when(repository.save(type)).thenReturn(type);

        RecipeType result = service.save(type);

        assertNotNull(result);
        assertEquals("Appetizer", result.getName());
        verify(repository, times(1)).save(type);
    }

    /**
     * Tests the deleteById method in the service.
     * Verifies that the correct method in the repository is called.
     */
    @Test
    void testDeleteById() {
        service.deleteById(1);
        verify(repository, times(1)).deleteById(1);
    }
}
