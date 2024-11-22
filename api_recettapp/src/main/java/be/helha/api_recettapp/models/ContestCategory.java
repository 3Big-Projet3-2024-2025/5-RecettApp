package be.helha.api_recettapp.models;

import jakarta.persistence.*;
import lombok.Data;


/**
 * Classe représentant une catégorie de concours.
 * Cette classe contient les informations de base
 * nécessaires pour définir une catégorie de concours.
 * Elle utilise Lombok pour générer automatiquement
 * les getters, setters, et méthodes utilitaires.
 */
@Data
@Entity
public class ContestCategory {

    /**
     * Identifiant unique de la catégorie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Titre de la catégorie.
     */
    private String title;

    /**
     * Description de la catégorie (facultatif).
     * Permet de fournir plus de détails sur la catégorie.
     */
    private String description;

    /**
     * Statut de la catégorie (actif ou inactif).
     */
    private boolean status;

    /**
     * Constructeur sans arguments.
     * Requis par JPA ou pour créer une instance vide.
     */
    public ContestCategory() {
        // Constructeur par défaut, vide
    }

    /**
     * Constructeur avec tous les arguments.
     * Permet d'initialiser tous les attributs de la classe.
     *
     * @param id    Identifiant unique de la catégorie.
     * @param title Titre de la catégorie.
     * @param description  Description de la catégorie.
     * @param status  Statut de la catégorie.
     */
    public ContestCategory(Long id, String title,
                           String description, boolean status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
