import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { Recipe } from '../models/recipe';
import { forkJoin, map, of } from 'rxjs';
import { FormsModule} from '@angular/forms';
import { CommonModule} from '@angular/common';
import { Router } from '@angular/router';
import { ImageServiceService } from '../services/image-service.service';

@Component({
  selector: 'app-recipe',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.css'
})


export class RecipeComponent {
  recipes: Recipe[] = [];
  isLoading = false;
  currentPage = 0;
  pageSize = 3;
  totalPages = 0;
  searchTerm: string = '';

  
  
  constructor(private service: RecipeService,private router:Router, private imaService: ImageServiceService) {}


   ngOnInit(): void {
    this.loadRecipes(this.currentPage, this.pageSize);
  }

  loadRecipes(page: number, size: number): void {
    this.isLoading = false;
    this.service.getRecipesPaginated(this.searchTerm, page, size).subscribe((data) => {
      this.totalPages = data.totalPages;
      this.currentPage = page; 
      const recipes: Recipe[] = data.content;
  
      const imageRequests = recipes.map((recipe) => {
        if (recipe.photo_url) {
          return this.imaService.getImage(recipe.photo_url).pipe(
            map((blob: Blob) => {
              recipe.photo_url = URL.createObjectURL(blob);
              return recipe;
            })
          );
        }
        return of(recipe);
      });
  
      forkJoin(imageRequests).subscribe((updatedRecipes) => {
        this.recipes = updatedRecipes;
        this.isLoading = true;
      });
    });
  }

  detailRecipe(id: number) : void {
    this.router.navigate(['/recipe', id]);
  }

  addRecipe(id: number) : void {
    this.router.navigate(['/recipe/add/', id]);
  }

  deleteRecipe(id: number) : void {
    this.service.deleteRecipe(id).subscribe({
      next: (value) =>{
        window.location.reload();
      },
      error: (err) => {
        console.log(err.error.message)
      }
    })
  }
  
  confirmDelete(id: number, title: string): void {
    const confirmed = window.confirm(`Are you sure you want to delete the recipe "${title}"?`);
    if (confirmed) {
      this.deleteRecipe(id);
    }
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.loadRecipes(page, this.pageSize);
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
    this.loadRecipes(this.currentPage, this.pageSize);
  }
}

