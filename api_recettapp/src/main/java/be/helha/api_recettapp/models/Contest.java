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
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Title of the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private String title;

    /**
     * The maximum number of participants who can take part in the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private int max_participants;

    /**
     * The beginning date of the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private Date start_date;

    /**
     * The deadline of the competition.
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private Date end_date;

    /**
     * The status of the competition, like "ongoing", "over", "not yet begun"
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     */
    @Setter
    private String status;

    /**
     * The category of the competition
     * It uses Lombok annotation {@code @Setter} to automatically generate a setter methods.
     * It uses JPA annotation {@code @ManyToOne} to automatically signal at the database the relation between tables.
     * It uses JPA annotation {@code @JoinColumn} to specify the foreign key (id_category_competition).
     */
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_category_competition")
    private ContestCategory category;

    /**
     * Check validity of a Contest Object
     * @param contest the contest to check
     * @return true
     * @throws IllegalArgumentException if an attribut is null or if start date is after end date
     */
    public static boolean checkContest(Contest contest) throws IllegalArgumentException{
        if (contest.getTitle() == null || contest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (contest.getMax_participants() <= 0) {
            throw new IllegalArgumentException("Max participants must be greater than 0.");
        }
        if (contest.getStart_date() == null || contest.getEnd_date() == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null.");
        }
        if (contest.getStart_date().after(contest.getEnd_date())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        if (contest.getStatus() == null || contest.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }
        /*if (contest.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }*/
        return true;
    }
}
