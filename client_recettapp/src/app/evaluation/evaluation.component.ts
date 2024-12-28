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
  currentEvaluation: Evaluation= this.initEvaluation();
  entries: any[] = [];
  recipes: any[] = [];
  showForm = false;
  isAdmin = true;


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


  initEvaluation(): Evaluation {
    return { rate: 0, entry: { id: 0 }, recipe: {
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
    } };
  }

  // Load reviews from the service
  loadEvaluations(): void {
    this.evaluationService.getAllEvaluations().subscribe({
      next: (data) => {
        this.evaluations = data;
      },
      error: (err) => {
        console.error('Error fetching evaluations:', err);
      }
    });
  }

  // Load entries from service
  loadEntries(): void {
    this.entryService.getAllEntries().subscribe({
      next: (data) => {
        this.entries = data;
      },
      error: (err) => {
        console.error('Error fetching entries:', err);
      }
    });
  }

  // Load recipes from the service
  loadRecipes(): void {
    this.recipeService.getAllRecipe().subscribe({
      next: (data) => {
        this.recipes = data;
      },
      error: (err) => {
        console.error('Error fetching recipes:', err);
      }
    });
  }

  // Save an assessment
  saveEvaluation(): void {
    this.evaluationService.addEvaluation(this.currentEvaluation).subscribe({
      next: () => {
        this.loadEvaluations();
        this.resetForm();
      },
      error: (err) => {
        console.error('Error adding evaluation:', err);
      }
    });
  }

  //Delete a review
  deleteEvaluation(id: number | undefined): void {
    if (confirm('Are you sure you want to delete this evaluation?')) {
      if(id)
      this.evaluationService.deleteEvaluation(id,this.isAdmin).subscribe({
        next: () => {
          this.loadEvaluations();
        },
        error: (err) => {
          console.error('Error deleting evaluation:', err);
        }
      });
    }
  }

  //Reset form
  resetForm(): void {
    this.currentEvaluation = this.initEvaluation();
    this.showForm = false;
  }



  // Toggle form display
  toggleForm(): void {
    this.showForm = !this.showForm;
  }
}
