import { Component } from '@angular/core';
import { Recipe } from '../models/recipe';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { KeycloakService } from 'keycloak-angular';
import { jwtDecode } from 'jwt-decode';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-recipe-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-recipe-list.component.html',
  styleUrl: './user-recipe-list.component.css'
})
export class UserRecipeListComponent {
  recipes: Recipe[] = [];
  currentPage: number = 0;
  pageSize: number = 10; 
  totalPages: number = 0;
  totalElements: number = 0;
  userMail: string = ''; 

  constructor(private recipeService: RecipeService,private authService: KeycloakService, private router: Router) {}

  ngOnInit(): void {
    this.getUserMail();
  }

  async getUserMail() {
    const token = await this.authService.getToken();
    const decodedToken: any = jwtDecode(token);
    this.userMail = decodedToken.email;
    this.getRecipesForUser(this.currentPage);
  }  

  getRecipesForUser(page: number): void {
    this.recipeService.getRecipesByUserMail(this.userMail, page, this.pageSize).subscribe({
      next: (data) => {
        this.recipes = data.content;
        this.currentPage = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: (err) => {
        console.error('Error fetching recipes:', err);
      },
    });
  }
  
  anonymizeRecipe(recipe: Recipe) {
    
    this.recipeService.anonymizeRecipe(recipe.id).subscribe({
      next: () => {
        console.log('Recipe anonymized successfully.');
        this.getRecipesForUser(this.currentPage); 
      },
      error: (err) => {
        console.error('Error anonymizing recipe:', err);
      },
    });
  }
  
  detailRecipe(id: number) : void {
    this.router.navigate(['/recipe', id]);
  }
}
