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
    private Long idCategoryContest;

    /**
     * Titre de la catégorie.
     */
    private String titleCategoryContest;

    /**
     * Description de la catégorie (facultatif).
     * Permet de fournir plus de détails sur la catégorie.
     */
    private String descriptionCategory;

    /**
     * Statut de la catégorie (actif ou inactif).
     */
    private boolean statusCategory;

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
     * @param idCategoryContest    Identifiant unique de la catégorie.
     * @param titleCategoryContest Titre de la catégorie.
     * @param descriptionCategory  Description de la catégorie.
     * @param statusCategory       Statut de la catégorie.
     */
    public ContestCategory(Long idCategoryContest, String titleCategoryContest,
                           String descriptionCategory, boolean statusCategory) {
        this.idCategoryContest = idCategoryContest;
        this.titleCategoryContest = titleCategoryContest;
        this.descriptionCategory = descriptionCategory;
        this.statusCategory = statusCategory;
    }
}
