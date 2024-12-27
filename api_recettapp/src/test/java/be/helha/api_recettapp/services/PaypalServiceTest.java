package be.helha.api_recettapp.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaypalServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaypalService paypalService;

    private final String clientId = "dummyClientId";
    private final String clientSecret = "dummyClientSecret";
    private final String apiUrl = "https://api-m.sandbox.paypal.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializing Mocks
        paypalService = new PaypalService(restTemplate, clientId, clientSecret, apiUrl); // Inject the service
    }


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
}
