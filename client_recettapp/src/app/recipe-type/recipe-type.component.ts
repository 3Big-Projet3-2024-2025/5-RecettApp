import { Component, OnInit } from '@angular/core';
import { RecipeTypeService } from '../services/recipe-type.service';
import { RecipeType } from '../models/recipe-type';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recipe-type',
  standalone:true,
  imports:[FormsModule,CommonModule],
  templateUrl: './recipe-type.component.html',
  styleUrls: ['./recipe-type.component.css'],
})
export class RecipeTypeComponent implements OnInit {
  recipeTypes: RecipeType[] = [];
  currentRecipeType: RecipeType = { name: '' };
  isEditing = false; 
  showForm = false; 

  constructor(private recipeTypeService: RecipeTypeService) {}

  ngOnInit(): void {
    this.loadRecipeTypes();
  }

  // Charger tous les types de recettes
  loadRecipeTypes(): void {
    this.recipeTypeService.getAllRecipeTypes().subscribe((data) => {
      this.recipeTypes = data;
    });
  }

  // Ouvrir le formulaire pour ajouter un type de recette
  addRecipeType(): void {
    this.currentRecipeType = { name: '' };
    this.isEditing = false;
    this.showForm = true;
  }

  // Charger les données pour édition
  editRecipeType(recipeType: RecipeType): void {
    this.currentRecipeType = { ...recipeType }; 
    this.isEditing = true;
    this.showForm = true;
  }

  // Supprimer un type de recette
  deleteRecipeType(id: number): void {
    if (confirm('Are you sure you want to delete this recipe type?')) {
      this.recipeTypeService.deleteRecipeType(id).subscribe(() => {
        this.loadRecipeTypes();
      });
    }
  }

  // Sauvegarder un type de recette (création ou mise à jour)
  saveRecipeType(): void {
    if (this.isEditing) {
      this.recipeTypeService
        .updateRecipeType(this.currentRecipeType.id!, this.currentRecipeType)
        .subscribe(() => {
          this.loadRecipeTypes();
          this.showForm = false;
        });
    } else {
      this.recipeTypeService.createRecipeType(this.currentRecipeType).subscribe(() => {
        this.loadRecipeTypes();
        this.showForm = false;
      });
    }
  }

  // Annuler l'édition/création
  cancel(): void {
    this.showForm = false;
    this.currentRecipeType = { name: '' };
  }
}
