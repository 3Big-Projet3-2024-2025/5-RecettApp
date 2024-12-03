import { Component } from '@angular/core';
import { RecipeService } from '../Service/recipe.service';
import { Recipe } from '../Interface/recipe';
import { Observable } from 'rxjs';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule, AsyncPipe, NgFor } from '@angular/common';

@Component({
  selector: 'app-recipe',
  standalone: true,
  imports: [CommonModule,AsyncPipe,FormsModule,NgFor],
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.css'
})


export class RecipeComponent {
  recipes!: Observable<Recipe[]> ;
  constructor(private service: RecipeService) {}


   ngOnInit(): void {
    this.recipes = this.service.getAllRecipe() as Observable<Recipe[]>;
  }
}
