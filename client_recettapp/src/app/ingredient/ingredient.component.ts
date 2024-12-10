import { Component, EventEmitter, Output } from '@angular/core';
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
  
  searchTerm: string = ''; 
  suggestions: any[] = []; // List of ingredient suggestions
  selectedIngredient: any = null;  
  currentIngredient: any = {
    ingredient: null,
    quantity: null,
    unit: ''
  };
  recipeComponents: any[] = [];
  @Output() recipeComponentsChange = new EventEmitter<any[]>();
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
      
      this.recipeComponents.push({ ...this.currentIngredient });
      this.recipeComponentsChange.emit(this.recipeComponents);
      
  
      
      this.currentIngredient = { ingredient: null, quantity: null, unit: '' };
      this.searchTerm = ''; 
      this.suggestions = []; 
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

    this.recipeComponents = this.recipeComponents.filter((ing) => ing !== ingredient);
    this.recipeComponentsChange.emit(this.recipeComponents);
  }

  
}
