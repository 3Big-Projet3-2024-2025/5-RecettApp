import { Component } from '@angular/core';
import { Recipe } from '../models/recipe';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { KeycloakService } from 'keycloak-angular';
import { jwtDecode } from 'jwt-decode';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-recipe-list',
  standalone: true,
  imports: [CommonModule,FormsModule],
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
  searchTerm: string = '';
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
    this.recipeService.getRecipesByUserMail(this.userMail, this.searchTerm, page, this.pageSize).subscribe({
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
  
 
  detailRecipe(id: number) {
    this.router.navigate(['recipe/detail', id, "backto"]);

  }
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.getRecipesForUser(page); 
    }
  }
  
  goToPrevious(): void {
    if (this.currentPage > 0) {
      this.goToPage(this.currentPage - 1);
    }
  }
  
  goToNext(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.goToPage(this.currentPage + 1);
    }
  }

  onSearch(): void {
    this.currentPage = 0; 
    this.getRecipesForUser(this.currentPage); 
  }

}
