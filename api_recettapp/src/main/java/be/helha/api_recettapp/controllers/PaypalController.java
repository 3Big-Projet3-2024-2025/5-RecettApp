package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.services.PaypalService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Controller class for handling PayPal-related endpoints.
 *
 * This class provides an endpoint for creating and initiating PayPal payments by interacting
 * with the {@link PaypalService}.
 * This class uses annotation {@code @RestController} to indicate that this class is a Spring MVC controller where each method's return value is directly written to the HTTP response body.
 * It uses annotation {@code @RequestMapping("/paypal")} to map HTTP requests to the "/paypal" URL path to methods in this controller.
 */
@RestController
@RequestMapping("/paypal")
public class PaypalController {
    private final PaypalService paypalService;

    /**
     * Constructs a new {@code PaypalController}.
     *
     * @param paypalService The {@link PaypalService} instance used to interact with the PayPal API.
     */
    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    /**
     * Initiates a PayPal payment and returns the approval URL.
     *
     * This endpoint handles GET requests to the {@code /pay} path. It creates a payment
     * with the specified total amount in USD and redirects the user to either a success or
     * cancellation URL depending on the payment outcome.
     *
     * @param total The total amount for the payment in USD.
     * @return A {@link String} containing the approval URL for the user to complete the payment.
     */
    @GetMapping("/pay")
    public ResponseEntity<Void> pay(@RequestParam double total) {
        String successUrl = "http://localhost:4200/success"; // URL Angular, not already defined
        String cancelUrl = "http://localhost:4200/cancel";

        String approvalUrl = paypalService.createPayment(total, "USD", cancelUrl, successUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(approvalUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
    }
}
