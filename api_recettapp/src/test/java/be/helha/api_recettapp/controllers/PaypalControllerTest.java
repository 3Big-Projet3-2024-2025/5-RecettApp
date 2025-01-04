package be.helha.api_recettapp.controllers;


import be.helha.api_recettapp.models.*;
import be.helha.api_recettapp.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the {@link PaypalController}.
 * Validates the behavior of the `pay` method under different scenarios.
 */
public class PaypalControllerTest {
    private static final Long USER_ID = 1L;
    private static final Integer CONTEST_ID = 1;
    private static final double PAYMENT_AMOUNT = 100.0;
    private static final String CURRENCY = "USD";
    private static final String REDIRECT_URL = "https://www.paypal.com/checkoutnow?token=example";

    @Mock
    private PaypalService paypalService;

    @Mock
    private EntryService entryService;

    @Mock
    private ContestService contestService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PaypalController paypalController;

    private Entry entry;
    private Users user;
    private Contest contest;

    /**
     * Initializes the test environment before each test.
     * Sets up mocks and test objects.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialization of test objects
        setupTestObjects();

        // Mock default configuration
        setupMocks();
    }

    /**
     * Sets up test objects used across test cases.
     */
    private void setupTestObjects() {
        user = new Users();
        user.setId(USER_ID);

        contest = new Contest();
        contest.setId(CONTEST_ID);

        entry = new Entry();
        entry.setUsers(user);
        entry.setContest(contest);
    }

    /**
     * Configures mock behavior for dependencies.
     */
    private void setupMocks() {
        when(userService.findById(USER_ID)).thenReturn(user);
        when(contestService.getContestById(CONTEST_ID)).thenReturn(Optional.of(contest));
        when(paypalService.createPayment(
                eq(PAYMENT_AMOUNT),
                eq(CURRENCY),
                anyString(),
                anyString()
        )).thenReturn(REDIRECT_URL);
    }

    /**
     * Tests the `pay` method with valid entry data.
     * Ensures that the payment is successfully initiated.
     */
    @Test
    void testPay_Success() {
        when(entryService.setUuid(any(Entry.class))).thenAnswer(invocation -> {
            Entry entry = invocation.getArgument(0);
            entry.setStatus("PENDING");
            return entry;
        });

        ResponseEntity<Map<String, String>> response = paypalController.pay(PAYMENT_AMOUNT, entry);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(REDIRECT_URL, response.getBody().get("approvalUrl"));

        verify(userService).findById(USER_ID);
        verify(contestService).getContestById(CONTEST_ID);
        verify(paypalService).createPayment(
                eq(PAYMENT_AMOUNT),
                eq(CURRENCY),
                anyString(),
                anyString()
        );
        verify(entryService).setUuid(any(Entry.class));
        verify(entryService).updateEntry(any(Entry.class));
    }

    /**
     * Tests the `pay` method with an invalid entry.
     * Verifies that an appropriate error response is returned.
     */
    @Test
    void testPay_InvalidEntry() {
        Entry invalidEntry = new Entry(); // Sans user ni contest

        ResponseEntity<Map<String, String>> response = paypalController.pay(PAYMENT_AMOUNT, invalidEntry);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid entry. Entry, user, or contest is null.", response.getBody().get("error"));

        verifyNoInteractions(paypalService, entryService);
    }

    /**
     * Tests the `pay` method when the user is not found.
     * Verifies that an appropriate error response is returned.
     */
    @Test
    void testPay_UserNotFound() {
        when(userService.findById(USER_ID)).thenReturn(null);

        ResponseEntity<Map<String, String>> response = paypalController.pay(PAYMENT_AMOUNT, entry);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found with ID: " + USER_ID, response.getBody().get("error"));

        verify(userService).findById(USER_ID);
        verifyNoInteractions(paypalService, entryService);
        verifyNoMoreInteractions(userService);
    }

    /**
     * Tests the `pay` method when the contest is not found.
     * Verifies that an appropriate error response is returned.
     */
    @Test
    void testPay_ContestNotFound() {
        when(contestService.getContestById(CONTEST_ID)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = paypalController.pay(PAYMENT_AMOUNT, entry);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contest not found with ID: " + CONTEST_ID, response.getBody().get("error"));

        verify(userService).findById(USER_ID);
        verify(contestService).getContestById(CONTEST_ID);
        verifyNoInteractions(paypalService, entryService);
    }

    /**
     * Unit tests for ContestCategoryController.
     */
    public static class ContestCategoryControllerTest {

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
}
