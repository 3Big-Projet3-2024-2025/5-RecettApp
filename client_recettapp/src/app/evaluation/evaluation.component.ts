import { Component, NgModule, OnInit } from '@angular/core';
import { Evaluation } from '../models/evaluation';
import { EvaluationService } from '../services/evaluation.service';
import { EntriesService } from '../services/entries.service';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Entry } from '../models/entry';
import { Recipe } from '../models/recipe';



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
    private recipeService: RecipeService
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
      approved: false,
      components: [],
      image: []
    }
    const evaluation : Evaluation = {
      id: 0,
      rate: 0,
      entry: entry,
      recipe: recipe
    }
    return evaluation;
  }



  loadEvaluations(): void {
    this.evaluationService.getAllEvaluations().subscribe({
      next: (data) => (this.evaluations = data),
      error: (err) => console.error('Erreur lors du chargement des évaluations :', err),
    });
  }

  loadEntries(): void {
    this.entryService.getAllEntries().subscribe({
      next: (data) => (this.entries = data),
      error: (err) => console.error('Erreur lors du chargement des entrées :', err),
    });
  }

  loadRecipes(): void {
    this.recipeService.getAllRecipe().subscribe({
      next: (data) => (this.recipes = data),
      error: (err) => console.error('Erreur lors du chargement des recettes :', err),
    });
  }

  deleteEvaluation(evaluation: Evaluation): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette évaluation ?')) {
      this.evaluationService.deleteEvaluation(evaluation.id!, this.isAdmin).subscribe({
        next: () => this.loadEvaluations(),
        error: (err) => console.error('Erreur lors de la suppression :', err),
      });
    }
  }

  saveEvaluation(): void {
    console.log(this.currentEvaluation);
    if (this.currentEvaluation.id) {
      if(this.currentEvaluation.entry){
        this.currentEvaluation.entry.id = this.selectedEntryId
      }
      if(this.selectedRecipeId && this.currentEvaluation.recipe){
        this.currentEvaluation.recipe.id = this.selectedRecipeId
      }
      this.evaluationService.addEvaluation(this.currentEvaluation).subscribe({
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

      this.evaluationService.addEvaluation(this.currentEvaluation).subscribe({
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




  editEvaluation(evaluation: Evaluation): void {
    this.currentEvaluation = { ...evaluation };
    this.selectedEntryId = evaluation.entry?.id || undefined;
    this.selectedRecipeId = evaluation.recipe?.id || undefined;
    this.showForm = true;
  }

  resetForm(): void {
    this.currentEvaluation = this.initEvaluation();
    this.selectedEntryId = undefined;
    this.selectedRecipeId = undefined;
  }

}
