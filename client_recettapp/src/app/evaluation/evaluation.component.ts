import { Component, NgModule, OnInit } from '@angular/core';
import { Evaluation } from '../models/evaluation';
import { EvaluationService } from '../services/evaluation.service';
import { EntriesService } from '../services/entries.service';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.css'],
  standalone: true,
  imports: [CommonModule,FormsModule],
})
export class EvaluationComponent implements OnInit {
  evaluations: Evaluation[] = [];
  currentEvaluation: Evaluation = this.initEvaluation();
  entries: any[] = [];
  recipes: any[] = [];
  showForm = false;
  isAdmin = true;
  selectedEntryId: number | null = null;
  selectedRecipeId: number | null = null;

  constructor(
    private evaluationService: EvaluationService,
    private entryService: EntriesService,
    private recipeService: RecipeService
  ) {}

  ngOnInit(): void {
    this.loadEntries();
    this.loadRecipes();
    this.loadEvaluations();
  }

  // Initialisation avec des valeurs par défaut
  initEvaluation(): Evaluation {
    return {
      rate: 0,
      entry: {
        id: 0,
        users: undefined,
        contest: undefined,
        status: ''
      },
      recipe: {
        id: 0,
        title: '',
        description: '',
        category: '',
        preparation_time: 0,
        cooking_time: 0,
        servings: 0,
        difficulty_level: '',
        instructions: '',
        approved: false,
        components: []
      }
    };
  }

  // Chargement des évaluations
  loadEvaluations(): void {
    this.evaluationService.getAllEvaluations().subscribe({
      next: (data) => (this.evaluations = data),
      error: (err) => console.error('Erreur lors du chargement des évaluations :', err),
    });
  }

  // Chargement des entrées
  loadEntries(): void {
    this.entryService.getAllEntries().subscribe({
      next: (data) => (this.entries = data),
      error: (err) => console.error('Erreur lors du chargement des entrées :', err),
    });
  }

  // Chargement des recettes
  loadRecipes(): void {
    this.recipeService.getAllRecipe().subscribe({
      next: (data) => (this.recipes = data),
      error: (err) => console.error('Erreur lors du chargement des recettes :', err),
    });
  }

  // Méthode pour supprimer une évaluation
  deleteEvaluation(evaluation: Evaluation): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette évaluation ?')) {
      this.evaluationService.deleteEvaluation(evaluation.id!, this.isAdmin).subscribe({
        next: () => this.loadEvaluations(),
        error: (err) => console.error('Erreur lors de la suppression :', err),
      });
    }
  }

  // Méthode pour ajouter ou modifier une évaluation
  saveEvaluation(): void {
    if (this.currentEvaluation.id) {
      // Modification d'une évaluation existante
      this.evaluationService.addEvaluation(this.currentEvaluation).subscribe({
        next: () => {
          this.loadEvaluations();
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => console.error('Erreur lors de la modification de l\'évaluation :', err),
      });
    } else {
      // Ajout d'une nouvelle évaluation
      this.evaluationService.addEvaluation(this.currentEvaluation).subscribe({
        next: () => {
          this.loadEvaluations();
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => console.error('Erreur lors de l\'ajout de l\'évaluation :', err),
      });
    }
  }

  // Méthode pour initialiser le formulaire pour l'édition
  editEvaluation(evaluation: Evaluation): void {
    this.currentEvaluation = { ...evaluation };
    this.selectedEntryId = evaluation.entry?.id || null;
    this.selectedRecipeId = evaluation.recipe?.id || null;
    this.showForm = true;
  }

  // Méthode pour réinitialiser le formulaire
  resetForm(): void {
    this.currentEvaluation = this.initEvaluation();
    this.selectedEntryId = null;
    this.selectedRecipeId = null;
  }

}
