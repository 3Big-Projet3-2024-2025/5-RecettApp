package be.helha.api_recettapp.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

        Map<String, String> amount = new HashMap<>();
        amount.put("total", String.format("%.2f", total));
        amount.put("currency", currency);

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("amount", amount);
        transaction.put("description", "Recipe contest");

        paymentData.put("transactions", new Map[]{transaction});

        Map<String, String> redirectUrls = new HashMap<>();
        redirectUrls.put("cancel_url", cancelUrl);
        redirectUrls.put("return_url", successUrl);
        paymentData.put("redirect_urls", redirectUrls);

        Map<String, String> pay = new HashMap<>();
        pay.put("payment_method", "paypal");
        paymentData.put("pay", pay);

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
}
