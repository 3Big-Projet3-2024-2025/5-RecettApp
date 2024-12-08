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

  fillSearchInput(suggestion: any) {
    this.searchTerm = suggestion.alimentName; 
    this.currentIngredient.ingredient = suggestion; 
    this.suggestions = []; 
  }

  
}
