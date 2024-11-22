package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents a competition.
 * This class is annotated with {@code @Entity}, indicating it is mapped to a database table.
 * It uses Lombok annotation {@code @Getter} to automatically generate all getter methods for each attribute.
 */
@Entity
@Getter
public class Contest {

    /**
     * Unique identifier for a competition.
     * It uses JPA annotation {@code @Id} to automatically signal at the database that is the primary key of the table.
     * It uses JPA annotation {@code @GeneratedValue(strategy = GenerationType.IDENTITY)} to automatically generate the primary key (id).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_competition;

    /**
     * Title of the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private String title_competition;

    /**
     * The maximum number of participants who can take part in the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private int max_participants_competition;

    /**
     * The beginning date of the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private Date start_date_competition;

    /**
     * The deadline of the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private Date end_date_competition;

    /**
     * The status of the competition, like "ongoing", "over", "not yet begun"
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private String status_competition;

    /**
     * The category of the competition
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     * It uses JPA annotation {@code @ManyToOne} to automatically signal at the database the relation between tables.
     * It uses JPA annotation {@code @JoinColumn} to specify the foreign key (id_category_competition).
     */
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_category_competition")
    private ContestCategory category_competition;
}
