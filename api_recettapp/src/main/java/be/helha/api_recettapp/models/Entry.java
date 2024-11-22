package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Contest link to the entry
     */
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    private String status;
}
