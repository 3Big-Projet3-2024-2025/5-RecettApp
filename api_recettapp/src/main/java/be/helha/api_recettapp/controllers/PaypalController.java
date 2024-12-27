package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.services.PaypalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for handling PayPal-related endpoints.
 *
 * This class provides an endpoint for creating and initiating PayPal payments by interacting
 * with the {@link PaypalService}.
 */
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
     * This endpoint handles GET requests to the {@code /paypal/pay} path. It creates a payment
     * with the specified total amount in USD and redirects the user to either a success or
     * cancellation URL depending on the payment outcome.
     *
     * @param total The total amount for the payment in USD.
     * @return A {@link String} containing the approval URL for the user to complete the payment.
     */
    @GetMapping("/paypal/pay")
    public String pay(@RequestParam double total) {
        String successUrl = "http://localhost:4200/success"; // URL Angular, not already defined
        String cancelUrl = "http://localhost:4200/cancel";
        return paypalService.createPayment(total, "USD", cancelUrl, successUrl);
    }
}
