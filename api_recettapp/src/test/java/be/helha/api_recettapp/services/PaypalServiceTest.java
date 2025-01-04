package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.PaypalResponse;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.PaypalResponseRepository;
import be.helha.api_recettapp.repositories.jpa.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the {@link PaypalService} class.
 * <p>This class contains unit tests for methods in the PaypalService, including
 * tests for payment creation, validation, token retrieval, and saving PayPal responses.</p>
 */
public class PaypalServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PaypalResponseRepository paypalResponseRepository;

    @Mock
    private UserRepository userRepository;

    private PaypalService paypalService;

    private final String clientId = "dummyClientId";
    private final String clientSecret = "dummyClientSecret";
    private final String apiUrl = "https://api-m.sandbox.paypal.com";

    /**
     * Set up method for the test, initializes mocks and the PaypalService instance.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializing Mocks
        paypalService = new PaypalService(restTemplate, clientId, clientSecret, apiUrl); // Inject the service
        ReflectionTestUtils.setField(paypalService, "userRepository", userRepository);
        ReflectionTestUtils.setField(paypalService, "paypalResponseRepository", paypalResponseRepository);
    }


    /**
     * Tests the {@link PaypalService#getAccessToken()} method.
     * <p>This test mocks a response from the PayPal OAuth2 token endpoint and verifies
     * that the method returns the expected access token.</p>
     */
    @Test
    void testGetAccessToken() {
        String tokenUrl = apiUrl + "/v1/oauth2/token";
        String expectedToken = "dummyAccessToken";

        // Mock JSON responses
        JsonNode tokenNode = mock(JsonNode.class);
        when(tokenNode.asText()).thenReturn(expectedToken);

        JsonNode responseBody = mock(JsonNode.class);
        when(responseBody.get("access_token")).thenReturn(tokenNode);

        ResponseEntity<JsonNode> response = new ResponseEntity<>(responseBody, HttpStatus.OK);

        // Mock RestTemplate behavior
        when(restTemplate.exchange(eq(tokenUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(response);

        // Call the method to test
        String actualToken = paypalService.getAccessToken();

        // Verifications
        assertEquals(expectedToken, actualToken);
        verify(restTemplate, times(1)).exchange(eq(tokenUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
    }


    /**
     * Tests the {@link PaypalService#createPayment(double, String, String, String)} method.
     * <p>This test mocks a response from PayPal to simulate payment creation, and verifies
     * that the correct approval URL is returned.</p>
     */
    @Test
    void testCreatePayment() {
        String paymentUrl = apiUrl + "/v1/payments/payment";
        String approvalUrl = "https://www.paypal.com/checkoutnow?token=example";
        String mockAccessToken = "mocked-access-token";

        // Mock for getAccessToken
        ResponseEntity<JsonNode> tokenResponse = new ResponseEntity<>(mock(JsonNode.class), HttpStatus.OK);
        when(restTemplate.exchange(
                contains("/v1/oauth2/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(JsonNode.class)
        )).thenReturn(tokenResponse);

        // Mock token response
        JsonNode tokenBody = tokenResponse.getBody();
        when(tokenBody.get("access_token")).thenReturn(mock(JsonNode.class));
        when(tokenBody.get("access_token").asText()).thenReturn(mockAccessToken);

        // Mock JsonNode for "links"
        JsonNode linkNode = mock(JsonNode.class);
        when(linkNode.get("rel")).thenReturn(mock(JsonNode.class));
        when(linkNode.get("rel").asText()).thenReturn("approval_url");
        when(linkNode.get("href")).thenReturn(mock(JsonNode.class));
        when(linkNode.get("href").asText()).thenReturn(approvalUrl);

        JsonNode linksNode = mock(JsonNode.class);
        when(linksNode.iterator()).thenReturn(List.of(linkNode).iterator());

        JsonNode responseBody = mock(JsonNode.class);
        when(responseBody.get("links")).thenReturn(linksNode);

        ResponseEntity<JsonNode> response = new ResponseEntity<>(responseBody, HttpStatus.CREATED);

        // Mock RestTemplate.exchange
        when(restTemplate.exchange(eq(paymentUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(response);

        // Call of the method
        String actualApprovalUrl = paypalService.createPayment(100.0, "USD", "http://localhost:4200/cancel", "http://localhost:4200/success");

        // Verifications
        assertEquals(approvalUrl, actualApprovalUrl);
        verify(restTemplate, times(1)).exchange(eq(paymentUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
    }

    /**
     * Tests the {@link PaypalService#validatePayment(String, String)} method.
     * <p>This test mocks a payment validation response from PayPal, and verifies
     * that the method returns true for a successful validation.</p>
     */
    @Test
    void testValidatePayment() {
        String paymentId = "PAY-123";
        String payerId = "PAYER-456";
        String validateUrl = apiUrl + "/v1/payments/payment/" + paymentId + "/execute";
        String mockAccessToken = "mocked-access-token";

        // Mock for getAccessToken
        ResponseEntity<JsonNode> tokenResponse = new ResponseEntity<>(mock(JsonNode.class), HttpStatus.OK);
        when(restTemplate.exchange(
                contains("/v1/oauth2/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(JsonNode.class)
        )).thenReturn(tokenResponse);

        JsonNode tokenBody = tokenResponse.getBody();
        when(tokenBody.get("access_token")).thenReturn(mock(JsonNode.class));
        when(tokenBody.get("access_token").asText()).thenReturn(mockAccessToken);

        // Mock for payment validation
        JsonNode stateNode = mock(JsonNode.class);
        when(stateNode.asText()).thenReturn("approved");

        JsonNode responseBody = mock(JsonNode.class);
        when(responseBody.get("state")).thenReturn(stateNode);

        ResponseEntity<JsonNode> response = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(eq(validateUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(response);

        boolean result = paypalService.validatePayment(paymentId, payerId);

        assertTrue(result);
        verify(restTemplate, times(1)).exchange(eq(validateUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
    }

    /**
     * Tests the {@link PaypalService#savePaypalResponse(Long, String)} method.
     * <p>This test mocks the repository behavior to save a PayPal response and verifies
     * that the saved response matches the input data.</p>
     */
    @Test
    void testSavePaypalResponse() {
        Long userId = 1L;
        String jsonResponse = "{\"payment_status\":\"completed\"}";

        Users mockUser = new Users();
        mockUser.setId(userId);

        PaypalResponse mockPaypalResponse = new PaypalResponse();
        mockPaypalResponse.setResponseJson(jsonResponse);
        mockPaypalResponse.setUser(mockUser);
        mockPaypalResponse.setResponseDate(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(paypalResponseRepository.save(any(PaypalResponse.class))).thenReturn(mockPaypalResponse);

        PaypalResponse result = paypalService.savePaypalResponse(userId, jsonResponse);

        assertNotNull(result);
        assertEquals(jsonResponse, result.getResponseJson());
        assertEquals(mockUser, result.getUser());
        verify(userRepository, times(1)).findById(userId);
        verify(paypalResponseRepository, times(1)).save(any(PaypalResponse.class));
    }

    /**
     * Tests the case when the user is not found in {@link PaypalService#savePaypalResponse(Long, String)}.
     * <p>This test ensures that if a user is not found, a RuntimeException is thrown.</p>
     */
    @Test
    void testSavePaypalResponseUserNotFound() {
        Long userId = 1L;
        String jsonResponse = "{\"payment_status\":\"completed\"}";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            paypalService.savePaypalResponse(userId, jsonResponse);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(paypalResponseRepository, never()).save(any(PaypalResponse.class));
    }

}
