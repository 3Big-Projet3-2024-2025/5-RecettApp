package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a PayPal response stored in the database.
 *
 * <p>This entity is used to store the response from PayPal when a payment request is made.
 * The response is stored as a JSON string, along with metadata such as the date and the associated user.</p>
 *
 * <p>Each {@link PaypalResponse} is linked to a specific user via the {@link Users} entity, which represents
 * the user who initiated the payment.</p>
 *
 * @see Users
 */
@Entity
@Getter
@Setter
public class PaypalResponse {

    /**
     * Unique identifier for the PayPal response.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The PayPal response stored as a JSON string.
     */
    private String responseJson;

    /**
     * The date and time when the PayPal response was received.
     */
    private LocalDateTime responseDate;

    /**
     * The {@link Users} entity associated with this PayPal response.
     * This defines the user who initiated the payment.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
