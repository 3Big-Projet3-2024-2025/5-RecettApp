import { Component, NgModule, OnInit } from '@angular/core';
import { Evaluation } from '../models/evaluation';
import { EvaluationService } from '../services/evaluation.service';
import { EntriesService } from '../services/entries.service';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Entry } from '../models/entry';
import { Recipe } from '../models/recipe';
import {KeycloakService} from "keycloak-angular";



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
  selectedEntryId: number | undefined;
  selectedRecipeId: number | undefined;

  constructor(
    private evaluationService: EvaluationService,
    private entryService: EntriesService,
    private recipeService: RecipeService,
    private keycloakService: KeycloakService
  ) {}

  ngOnInit(): void {
    this.loadEntries();
    this.loadRecipes();
    this.loadEvaluations();
    this.resetForm();
  }

  initEvaluation(): Evaluation {
    const entry : Entry = {

    }
    const recipe : Recipe = {
      id: 0,
      title: '',
      description: '',
      category: '',
      preparation_time: 0,
      cooking_time: 0,
      servings: 0,
      difficulty_level: '',
      instructions: '',
      components: [],
      image: [],
      masked: false
    }
    const evaluation : Evaluation = {
      id: 0,
      rate: 0,
      entry: entry,
      recipe: recipe
    }
    return evaluation;
  }



  async loadEvaluations(): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.evaluationService.getAllEvaluations(token).subscribe({
      next: (data) => (this.evaluations = data),
      error: (err) => console.error('Erreur lors du chargement des évaluations :', err),
    });
  }
  toggleForm(): void {
    this.showForm = !this.showForm;
  }
  

  async loadEntries(): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.entryService.getAllEntries(token).subscribe({
      next: (data) => (this.entries = data),
      error: (err) => console.error('Erreur lors du chargement des entrées :', err),
    });
  }

  async loadRecipes(): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.recipeService.getAllRecipe(token).subscribe({
      next: (data) => (this.recipes = data),
      error: (err) => console.error('Erreur lors du chargement des recettes :', err),
    });
  }

  async deleteEvaluation(evaluation: Evaluation): Promise<void> {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette évaluation ?')) {
      const token = await this.keycloakService.getToken();
      this.evaluationService.deleteEvaluation(evaluation.id!, this.isAdmin, token).subscribe({
        next: () => this.loadEvaluations(),
        error: (err) => console.error('Erreur lors de la suppression :', err),
      });
    }
  }

  async saveEvaluation(): Promise<void> {
    const token = await this.keycloakService.getToken();
    console.log(this.currentEvaluation);
    if (!this.currentEvaluation.id) {
      if (this.currentEvaluation.entry) {
        this.currentEvaluation.entry.id = this.selectedEntryId
      }
      if (this.selectedRecipeId && this.currentEvaluation.recipe) {
        this.currentEvaluation.recipe.id = this.selectedRecipeId
      }
      this.evaluationService.addEvaluation(this.currentEvaluation, token).subscribe({
        next: () => {
          this.loadEvaluations();
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => {
          console.error('Erreur lors de la modification de l\'évaluation :', err);
        },
      });
    } else {
      this.evaluationService.addEvaluation(this.currentEvaluation, token).subscribe({
        next: () => {
          this.loadEvaluations();
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => {
          console.error('Erreur lors de l\'ajout de l\'évaluation :', err);
        },
      });
    }
  }


  resetForm(): void {
    this.currentEvaluation = this.initEvaluation();
    this.selectedEntryId = undefined;
    this.selectedRecipeId = undefined;
  }

}
