package be.helha.api_recettapp.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
     * Validates a PayPal payment by executing the payment using the provided payment ID and payer ID.
     *
     * This method interacts with the PayPal API to execute the payment and checks if the payment has been approved.
     * It sends a request to the PayPal API with the payment ID and payer ID, and based on the response, it determines
     * whether the payment was successfully approved.
     *
     * @param paymentId The unique PayPal payment ID associated with the payment to be validated.
     * @param payerId The PayPal payer ID associated with the payment to be validated.
     * @return {@code true} if the payment has been successfully approved, otherwise {@code false}.
     * @throws RuntimeException If the payment validation fails or the PayPal API returns an error.
     */
    public boolean validatePayment(String paymentId, String payerId) {
        String url = apiUrl + "/v1/payments/payment/" + paymentId + "/execute";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payer = new HashMap<>();
        payer.put("payer_id", payerId);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payer, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, entity, JsonNode.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("state").asText().equals("approved");
        }

        throw new RuntimeException("Payment validation failed");
    }

}
