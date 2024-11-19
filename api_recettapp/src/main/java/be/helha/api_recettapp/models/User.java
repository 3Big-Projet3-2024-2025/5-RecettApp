package be.helha.api_recettapp.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_user;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private String statutUtilisateur;


    @Column(nullable = false)
    private LocalDate dateInscription;
    private String numeroTelephone;


    @OneToMany(mappedBy = "user")
    private List<Entry> inscriptions;

    @OneToMany(mappedBy = "user")
    private List<Role> roles;


}
