package be.helha.api_recettapp.controllers;


import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
}
