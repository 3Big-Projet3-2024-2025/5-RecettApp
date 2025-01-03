package be.helha.api_recettapp.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

/**
 * This class reprents each entry for an user to a contest
 */
@Data
@Entity
public class Entry {
    /**
     * Id of entry
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * User link to the entry
     */
    @ManyToOne
    private Users users;

    /**
     * Contest link to the entry
     */
    @ManyToOne
    private Contest contest;

    private String status;

    @Nullable
    private UUID uuid;



}
