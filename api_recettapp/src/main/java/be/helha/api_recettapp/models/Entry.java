package be.helha.api_recettapp.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

/**
 * Represents an entry for a user in a contest.
 * Each entry is associated with a specific user, contest, and may include a unique identifier and status.
 */
@Data
@Entity
public class Entry {

    /**
     * Unique identifier for the entry.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The user associated with this entry.
     */
    @ManyToOne
    private Users users;

    /**
     * The contest associated with this entry.
     */
    @ManyToOne
    private Contest contest;

    /**
     * The status of the entry.
     * This can represent the current state of the entry in the contest.
     */
    private String status;

    /**
     * An optional unique identifier (UUID) for the entry.
     * This field can be used for external references or tracking purposes.
     */
    @Nullable
    private UUID uuid;
}
