package be.helha.api_recettapp.services;

import be.helha.api_recettapp.models.PaypalResponse;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.repositories.jpa.PaypalResponseRepository;
import be.helha.api_recettapp.repositories.jpa.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service class for integrating with PayPal's API.
 *
 * This service provides functionality to interact with the PayPal API, including obtaining access tokens
 * and creating payments.
 * It uses {@code @Service} annotation to mark this class as a Spring service component.
 */
@Service
public class PaypalService {
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String apiUrl;

    @Autowired
    private PaypalResponseRepository paypalResponseRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Constructs a new {@code PaypalService}.
     *
     * @param restTemplate A {@link RestTemplate} instance for making HTTP requests.
     * @param clientId The PayPal client ID, injected using {@code @Qualifier("getClientId")}.
     * @param clientSecret The PayPal client secret, injected using {@code @Qualifier("getClientSecret")}.
     * @param apiUrl The PayPal API base URL, injected using {@code @Qualifier("getApiUrl")}.
     */
    public PaypalService(RestTemplate restTemplate,
                         @Qualifier("getClientId") String clientId,
                         @Qualifier("getClientSecret") String clientSecret,
                         @Qualifier("getApiUrl") String apiUrl) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.apiUrl = apiUrl;
    }

    /**
     * Retrieves an access token from PayPal for API authentication.
     *
     * Sends a POST request to the PayPal API's OAuth2 token endpoint with the client credentials
     * to obtain an access token.
     *
     * @return The PayPal access token as a {@link String}.
     * @throws RuntimeException If the token retrieval fails or the response is invalid.
     */
    public String getAccessToken() {
        String url = apiUrl + "/v1/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, entity, JsonNode.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("access_token").asText();
        }

        throw new RuntimeException("Failed to fetch PayPal access token");
    }

    /**
     * Creates a payment on PayPal and retrieves the approval URL.
     *
     * Sends a POST request to the PayPal API to create a payment with the specified details.
     *
     * @param total The total amount for the payment.
     * @param currency The currency for the payment (e.g., "USD").
     * @param cancelUrl The URL to redirect the user to if the payment is canceled.
     * @param successUrl The URL to redirect the user to if the payment is successful.
     * @return The approval URL for the created payment, which the user must visit to complete the payment.
     * @throws RuntimeException If the payment creation fails or the response is invalid.
     */
    public String createPayment(double total, String currency, String cancelUrl, String successUrl) {
        String url = apiUrl + "/v1/payments/payment";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("intent", "sale");

        DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
        String formattedTotal = df.format(total);

        Map<String, String> amount = new HashMap<>();
        amount.put("total", formattedTotal);
        amount.put("currency", currency);

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("amount", amount);
        transaction.put("description", "Recipe contest");

        paymentData.put("transactions", List.of(transaction));

        Map<String, String> redirectUrls = new HashMap<>();
        redirectUrls.put("cancel_url", cancelUrl);
        redirectUrls.put("return_url", successUrl);
        paymentData.put("redirect_urls", redirectUrls);

        Map<String, String> payer = new HashMap<>();
        payer.put("payment_method", "paypal");
        paymentData.put("payer", payer);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentData, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, entity, JsonNode.class);

        if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
            for (JsonNode link : response.getBody().get("links")) {
                if (link.get("rel").asText().equals("approval_url")) {
                    return link.get("href").asText();
                }
            }
        }

        throw new RuntimeException("Failed to create PayPal payment");
    }

    /**
     * Saves the PayPal response data associated with a user.
     *
     * <p>This method retrieves a user by their ID, creates a new {@link PaypalResponse} entity with the provided
     * PayPal response JSON, and associates the response with the retrieved user. The PayPal response is then saved
     * in the database.</p>
     *
     * <p>If the user with the given ID does not exist, a {@link RuntimeException} is thrown.</p>
     *
     * @param userId The ID of the user to associate with the PayPal response.
     * @param jsonResponse The PayPal response JSON to be saved.
     * @return The saved {@link PaypalResponse} entity.
     * @throws RuntimeException If the user with the specified ID is not found.
     */
    public PaypalResponse savePaypalResponse(Long userId, String jsonResponse) {
        // Get the user
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Create and save the PayPal Response
        PaypalResponse paypalResponse = new PaypalResponse();
        paypalResponse.setResponseJson(jsonResponse);
        paypalResponse.setResponseDate(LocalDateTime.now());
        paypalResponse.setUser(user);

        return paypalResponseRepository.save(paypalResponse);
    }
}
