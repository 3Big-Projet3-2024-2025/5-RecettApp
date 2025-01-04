package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.AppError;
import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.models.RecipeType;
import be.helha.api_recettapp.services.IContestService;
import be.helha.api_recettapp.services.RecipeTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class of ContestController
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TestContestController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IContestService contestService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Create a sample of contest
     * @return @type Contest contest object
     * @throws Exception
     */
    private Contest createSampleContest() throws Exception {
        ContestCategory category = new ContestCategory();
        category.setId(1L);
        category.setTitle("Cooking");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Contest contest = new Contest();
        contest.setId(1);
        contest.setTitle("Cooking Contest");
        contest.setMax_participants(50);
        contest.setStart_date(sdf.parse("2024-01-01"));
        contest.setEnd_date(sdf.parse("2024-01-10"));
        contest.setStatus("not yet begun");
        contest.setCategory(category);

        return contest;
    }

    /**
     * Test getContests controller
     * @throws Exception
     */
    @Test
    void testGetAllContests() throws Exception {
        Contest contest1 = createSampleContest();
        Contest contest2 = createSampleContest();
        contest2.setId(2);
        contest2.setTitle("Art Contest");

        List<Contest> contests = Arrays.asList(contest1, contest2);

        // Mock the contestService
        given(contestService.getContests()).willReturn(contests);

        mockMvc.perform(get("/contests/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Cooking Contest"))
                .andExpect(jsonPath("$[1].title").value("Art Contest"));
    }

    /**
     * Test getContestsByName controller
     * @throws Exception
     */
    @Test
    void testGetContestByTitle() throws Exception {
        Contest contest = createSampleContest();
        List<Contest> contests = List.of(contest);

        given(contestService.getContestByTitle("Cooking Contest")).willReturn(contests);

        mockMvc.perform(get("/contests/Cooking Contest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Cooking Contest"));
    }

    /**
     * Test addContest controller
     * @throws Exception
     */
    @Test
    void testAddContest() throws Exception {
        Contest contest = createSampleContest();

        given(contestService.addContest(Mockito.any(Contest.class))).willReturn(contest);

        mockMvc.perform(post("/contests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cooking Contest"))
                .andExpect(jsonPath("$.max_participants").value(50));
    }

    /**
     * Test updateContest controller
     * @throws Exception
     */
    @Test
    void testUpdateContest() throws Exception {
        Contest contest = createSampleContest();
        contest.setTitle("Updated Contest");

        given(contestService.updateContest(Mockito.any(Contest.class))).willReturn(contest);

        mockMvc.perform(put("/contests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Contest"));
    }

    /**
     * Test deleteContest controller
     * @throws Exception
     */
    @Test
    void testDeleteContest() throws Exception {
        Mockito.doNothing().when(contestService).deleteContest(1);

        mockMvc.perform(delete("/contests/1"))
                .andExpect(status().isOk());
    }

    /**
     * Test class for RecipeTypeController.
     * This class tests all the methods in the RecipeTypeController.
     */
    static class RecipeTypeControllerTest {

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
}
