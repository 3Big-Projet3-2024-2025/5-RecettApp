package be.helha.api_recettapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for PayPal integration settings.
 *
 * <p>This class provides configuration properties and beans required for interacting with the PayPal API.
 * The configuration values are loaded from the application's properties file using the {@code @Value} annotation.</p>
 */
@Configuration
public class PaypalConfig {

    /**
     * PayPal client ID for API authentication.
     */
    @Value("${paypal.client-id}")
    private String clientId;

    /**
     * PayPal client secret for API authentication.
     */
    @Value("${paypal.client-secret}")
    private String clientSecret;

    /**
     * Base URL of the PayPal API.
     */
    @Value("${paypal.api-url}")
    private String apiUrl;

    /**
     * Creates a {@link RestTemplate} bean for making HTTP requests to the PayPal API.
     *
     * @return A {@link RestTemplate} instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Retrieves the PayPal client ID.
     *
     * @return The PayPal client ID.
     */
    @Bean
    public String getClientId() {
        return clientId;
    }

    /**
     * Retrieves the PayPal client secret.
     *
     * @return The PayPal client secret.
     */
    @Bean
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Retrieves the PayPal API base URL.
     *
     * @return The PayPal API base URL.
     */
    @Bean
    public String getApiUrl() {
        return apiUrl;
    }
}
