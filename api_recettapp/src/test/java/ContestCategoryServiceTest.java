import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.repositories.jpa.ContestCategoryRepository;
import be.helha.api_recettapp.services.ContestCategoryService;
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
 * Unit tests for ContestCategoryService.
 * This class tests the methods in the ContestCategoryService class
 * using Mockito to mock the repository layer.
 */
class ContestCategoryServiceTest {

    /**
     * Mocked instance of ContestCategoryRepository.
     */
    @Mock
    private ContestCategoryRepository repository;

    /**
     * Service being tested, with the repository mocked.
     */
    @InjectMocks
    private ContestCategoryService service;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the findAll method in the service.
     * Verifies that the method retrieves all contest categories.
     */
    @Test
    void testFindAll() {
        ContestCategory category1 = new ContestCategory(1L, "Desserts", "All dessert recipes", true);
        ContestCategory category2 = new ContestCategory(2L, "Main Courses", "Main course recipes", true);

        when(repository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<ContestCategory> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    /**
     * Tests the findById method in the service when the contest category exists.
     * Verifies that the correct contest category is returned.
     */
    @Test
    void testFindById() {
        ContestCategory category = new ContestCategory(1L, "Desserts", "All dessert recipes", true);

        when(repository.findById(1L)).thenReturn(Optional.of(category));

        Optional<ContestCategory> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Desserts", result.get().getTitle());
        verify(repository, times(1)).findById(1L);
    }

    /**
     * Tests the save method in the service.
     * Verifies that a contest category is correctly saved.
     */
    @Test
    void testSave() {
        ContestCategory category = new ContestCategory(null, "Desserts", "All dessert recipes", true);

        when(repository.save(category)).thenReturn(category);

        ContestCategory result = service.save(category);

        assertNotNull(result);
        assertEquals("Desserts", result.getTitle());
        verify(repository, times(1)).save(category);
    }

    /**
     * Tests the deleteById method in the service.
     * Verifies that the correct method in the repository is called.
     */
    @Test
    void testDeleteById() {
        service.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
