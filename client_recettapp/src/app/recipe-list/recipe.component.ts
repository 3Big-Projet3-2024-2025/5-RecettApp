import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { Recipe } from '../models/recipe';
import { forkJoin, map } from 'rxjs';
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
  constructor(private service: RecipeService,private router:Router, private imaService: ImageServiceService) {}


   ngOnInit(): void {
    this.isLoading = false;
    
    this.service.getAllRecipe().subscribe(data => {
      const imageRequests = data.map(recipe => {
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

}
function of(recipe: Recipe): any {
  throw new Error('Function not implemented.');
}

