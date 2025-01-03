import { Component } from '@angular/core';
import { Recipe } from '../models/recipe';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { KeycloakService } from 'keycloak-angular';
import { jwtDecode } from 'jwt-decode';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-recipe-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-recipe-list.component.html',
  styleUrl: './user-recipe-list.component.css'
})
export class UserRecipeListComponent {
  recipes: Recipe[] = [];
  userMail: string = ''; 

  constructor(private recipeService: RecipeService,private authService: KeycloakService) {}

  ngOnInit(): void {
    this.getUserMail();
  }

  async getUserMail() {
    const token = await this.authService.getToken();
    const decodedToken: any = jwtDecode(token);
    this.userMail = decodedToken.email;
    this.getRecipesForUser();
  }  

  getRecipesForUser(): void {
    this.recipeService.getRecipesByUserMail(this.userMail).subscribe({
      next: (data) => this.recipes = data,
      error: (error) => console.error('Error fetching recipes:', error)
    });
  }

  anonymizeRecipe(){
    
  }
}
