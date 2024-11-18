package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    private String status;
}
