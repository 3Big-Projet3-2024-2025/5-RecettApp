package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Data;

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
}
