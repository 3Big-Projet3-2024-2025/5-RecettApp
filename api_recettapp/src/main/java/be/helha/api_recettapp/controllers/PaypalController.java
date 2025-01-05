package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.models.PaypalResponse;
import be.helha.api_recettapp.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller class for handling PayPal-related endpoints.
 * <p>
 * This class provides an endpoint for creating and initiating PayPal payments by interacting
 * with the {@link PaypalService}.
 * This class uses annotation {@code @RestController} to indicate that this class is a Spring MVC controller where each method's return value is directly written to the HTTP response body.
 * It uses annotation {@code @RequestMapping("/paypal")} to map HTTP requests to the "/paypal" URL path to methods in this controller.
 */
@RestController
@RequestMapping("/paypal")
@CrossOrigin(origins = "http://localhost:4200")
public class PaypalController {
    private final PaypalService paypalService;
    private final IEntryService entryService;
    private final IContestService contestService;
    private final UserService userService;


    /**
     * Constructs a new {@code PaypalController}.
     *
     * @param paypalService The {@link PaypalService} instance used to interact with the PayPal API.
     * @param entryService The {@link EntryService} instance used to interact with the Entry Service.
     * @param contestService The {@link ContestService} instance used to interact with the Contest Service.
     * @param userService The {@link UserService} instance used to interact with the Contest Service.
     */
    public PaypalController(PaypalService paypalService, IEntryService entryService, IContestService contestService, UserService userService) {
        this.paypalService = paypalService;
        this.entryService = entryService;
        this.contestService = contestService;
        this.userService = userService;
    }

    /**
     * Initiates a PayPal payment and returns the approval URL.
     * <p>
     * This endpoint handles Post requests to the {@code /pay} path. It creates a payment
     * with the specified total amount in USD and redirects the user to either a success or
     * cancellation URL depending on the payment outcome.
     *
     * @param total The total amount for the payment in USD.
     * @param entry The entry to registered
     * @return A {@link String} containing the approval URL for the user to complete the payment.
     */
    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> pay(@RequestParam double total, @RequestBody Entry entry) {

        try {
            // check if entry is valid
            if (entry == null || entry.getContest() == null || entry.getUsers() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid entry. Entry, user, or contest is null."));
            }

            // check user
            long idUser = (long) entry.getUsers().getId();
            Users user = userService.findById(idUser);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found with ID: " + entry.getUsers().getId()));
            }

            // check contest
            Optional<Contest> contest = contestService.getContestById(entry.getContest().getId());
            if (contest.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Contest not found with ID: " + entry.getContest().getId()));
            }

            // check if user has already this entry
            Entry existingEntry = entryService.findByUserAndContest(entry.getUsers(), entry.getContest());
            if (existingEntry != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "User already registered for this contest."));
            }

            // set Random UUID
            Entry updatedEntry = entryService.setUuid(entry);
            updatedEntry.setStatus("waiting");
            UUID uuid = updatedEntry.getUuid();
            entryService.updateEntry(updatedEntry);

            // Redirect to these url
            String successUrl = "http://localhost:4200/success?uuid=" + uuid;
            String cancelUrl = "http://localhost:4200/cancel?uuid=" + uuid;

            // Get PAYPAL url
            String approvalUrl = paypalService.createPayment(total, "USD", cancelUrl, successUrl);

            // Create response
            Map<String, String> response = new HashMap<>();
            response.put("approvalUrl", approvalUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while processing the payment: " + e.getMessage()));
        }
    }

    /**
     * Validates a PayPal payment by checking the payment status using the provided payment ID and payer ID.
     * <p>
     * This endpoint handles GET requests to the {@code /validate} path. It verifies if the payment was successfully
     * completed by interacting with the PayPal service. The validation checks if the provided {@code paymentId}
     * and {@code payerId} match a successful transaction.
     *
     * @param paymentId The unique PayPal payment ID associated with the payment.
     * @param payerId   The PayPal payer ID associated with the payment.
     * @return A {@link ResponseEntity} containing a {@code Boolean} value indicating whether the payment was
     * successful. If the payment is successful, it returns {@code true}, otherwise {@code false}.
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validatePayment(
            @RequestParam UUID uuid,
            @RequestParam String paymentId,
            @RequestParam String payerId) {

        try {
            boolean isPaymentSuccessful = paypalService.validatePayment(paymentId, payerId);
            boolean uuidExist = false;
            if (isPaymentSuccessful) {

                Entry entry = entryService.getEntryByUuid(uuid);
                if (entry != null) {
                    // remove the UUID
                    entryService.removeUuid(uuid);
                    entry.setStatus("registered");
                    entryService.updateEntry(entry);
                    // save paypal payment
                } else {

                    return ResponseEntity.ok(!uuidExist);
                }
            }
            return ResponseEntity.ok(isPaymentSuccessful);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }

    /**
     * Endpoint to save the PayPal response associated with a user.
     *
     * <p>This endpoint receives a user ID and the PayPal response JSON, and it calls the service layer to
     * save the PayPal response to the database. The PayPal response is associated with the user identified by the given user ID.</p>
     *
     * <p>The method accepts the user ID as a query parameter and the PayPal response JSON as the request body.</p>
     *
     * @param userId       The ID of the user to associate with the PayPal response.
     * @param jsonResponse The PayPal response JSON to be saved.
     * @return The saved {@link PaypalResponse} entity.
     */
    @PostMapping("/response")
    public PaypalResponse savePaypalResponse(@RequestParam Long userId, @RequestBody String jsonResponse) {
        return paypalService.savePaypalResponse(userId, jsonResponse);
    }
}
