import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule, AsyncPipe, NgFor } from '@angular/common';
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
  recipes!: Observable<Recipe[]> ;
  imageRecipe!: null | string;
  constructor(private service: RecipeService,private router:Router, private imaService: ImageServiceService) {}


   ngOnInit(): void {
    this.recipes = this.service.getAllRecipe() as Observable<Recipe[]>;
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

  getImage(imageName: string){
    this.imaService.getImage(imageName).subscribe(
      (next: Blob) => {
       
        this.imageRecipe = URL.createObjectURL(next);
      },
      (err) => {
       console.log(err.error.message)
      }
    );
  }
}
