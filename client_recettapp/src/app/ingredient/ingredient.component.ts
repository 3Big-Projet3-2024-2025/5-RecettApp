import { Component } from '@angular/core';
import { IngredientService } from '../services/ingredient.service';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-ingredient',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './ingredient.component.html',
  styleUrl: './ingredient.component.css'
})
export class IngredientComponent {
  
  searchTerm: string = ''; // sear Term
  suggestions: any[] = []; // List of ingredient suggestions
  ingredients: any[] = [];
  selectedIngredient: any = null;
  currentIngredient: any = {
    ingredient: null,
    quantity: null,
    unit: ''
  };


  constructor(private ingredientService: IngredientService) {}

  onSearch() {
    if (this.searchTerm.length > 2) {
      this.ingredientService.searchIngredients(this.searchTerm).subscribe({
        next: (data) => {
          this.suggestions = data;
        },
        error: (err) => {
          console.error('Error fetching ingredients:', err);
        }
      });
    } else {
      this.suggestions = [];
    }
  }

  addIngredient() {
    if (
      this.currentIngredient.ingredient &&
      this.currentIngredient.quantity &&
      this.currentIngredient.unit
    ) {
      // Ajouter l'ingrédient à la liste
      this.ingredients.push({ ...this.currentIngredient });
  
      // Réinitialiser les champs après ajout
      this.currentIngredient = { ingredient: null, quantity: null, unit: '' };
      this.searchTerm = ''; // Réinitialise le champ de recherche
      this.suggestions = []; // Vide les suggestions
    } else {
      alert('Please fill in all fields before adding the ingredient.');
    }
  }

  fillSearchInput(suggestion: any) {
    this.searchTerm = suggestion.alimentName; 
    this.currentIngredient.ingredient = suggestion; 
    this.suggestions = []; 
  }

  removeIngredient(ingredient: any) {
    this.ingredients = this.ingredients.filter((ing) => ing !== ingredient);
  }

  
}
