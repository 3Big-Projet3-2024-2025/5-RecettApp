import { Component, OnInit } from '@angular/core';
import { Recipe } from '../models/recipe';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { CommonModule } from '@angular/common';
import { RecipeComponentService } from '../services/recipe_Service/recipe-component.service';
import { ImageServiceService } from '../services/image-service.service';

@Component({
  selector: 'app-recipe-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.css'
})
export class RecipeDetailComponent implements OnInit {
  recipe?: Recipe;
  imageRecipe: string | null = null;
  constructor(private route: ActivatedRoute,private router: Router, private service: RecipeService, private serviceRecipeComponent: RecipeComponentService
              ,private imaService: ImageServiceService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.service.getRecipeById(+id).subscribe(
        (data) => {
        this.recipe = data;
        if (data.photo_url) {
          this.getImage(data.photo_url);
        }
        this.getRecipeComponent(+id)
        },(err) => {
          console.log(err.error.message)
        }
      );}
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
  getRecipeComponent(id : number){
    this.serviceRecipeComponent.getRecipeComponentsByIdRecipe(+id).subscribe(
      (data) => {
      if (this.recipe) {
        this.recipe.components = data;
      }
    }, (err) => {
      console.log(err.error.message)
     }
    ); 
  }

  backRecipeList(): void {
    this.router.navigate(['/recipe']);
  }
}
