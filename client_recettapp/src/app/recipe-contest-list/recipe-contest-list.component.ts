import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Recipe } from '../models/recipe';
import { Router } from '@angular/router';
import { map, of, forkJoin } from 'rxjs';
import { ImageServiceService } from '../services/image-service.service';
import { RecipeService } from '../services/recipe_Service/recipe.service';

@Component({
  selector: 'app-recipe-contest-list',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './recipe-contest-list.component.html',
  styleUrl: './recipe-contest-list.component.css'
})
export class RecipeContestListComponent {

recipes: Recipe[] = [];
constructor(private service: RecipeService, private imaService: ImageServiceService) {}


   ngOnInit(): void {
    
    this.service.getRecipeByIdContest(32).subscribe(data => {
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
      });
    });
  }

confirmDelete(arg0: any,arg1: any) {
throw new Error('Method not implemented.');
}
detailRecipe(arg0: any) {
throw new Error('Method not implemented.');
}
addRecipe() {
throw new Error('Method not implemented.');
}

}
