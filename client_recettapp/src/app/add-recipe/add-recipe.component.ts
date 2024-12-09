import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from '../models/recipe';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { Contest } from '../models/contest';
import { IngredientComponent } from "../ingredient/ingredient.component";
import { RecipeComponentService } from '../services/recipe_Service/recipe-component.service';
import { RecipeComponent } from '../models/recipe-component';

@Component({
  selector: 'app-add-recipe',
  standalone: true,
  imports: [CommonModule, FormsModule, IngredientComponent],
  templateUrl: './add-recipe.component.html',
  styleUrl: './add-recipe.component.css'
})
export class AddRecipeComponent {

  constructor(private route: ActivatedRoute,private recipeService: RecipeService, private componentService: RecipeComponentService,private router: Router){}
  ngOnInit(): void {
    const contestId = this.route.snapshot.paramMap.get('idConstest');
    if (contestId != null) {
      this.contestRecupe.id = +contestId
      this.recipeToAdd.contest =  this.contestRecupe
    }
  }

  contestRecupe : Contest = {
    title: '',
    max_participants: 0,
    status: ''
  }

  recipeToAdd: Recipe = {
    id: 0, 
    title: '',
    description: '',
    category: '',
    preparation_time: 0,
    cooking_time: 0,
    servings: 0,
    difficulty_level: '',
    instructions: '',
    approved: false,
    photo_url: '',
    contest: undefined,
    components: [],
    recipe_type: undefined
  };

  onSubmit(): void {
      this.recipeService.addRecipe(this.recipeToAdd).subscribe(
        {
          next: (value: Recipe) => {
            this.recipeToAdd.id = value.id; // Recovery of the recipe updated with its new id
            this.addRecipeComponent();
            this.router.navigate(['/recipe']);
          },
          error: (err) => {
            console.log(err.error.message)
          },
        }
      )
        //console.log(this.recipeToAdd)
  }

  onRecipeComponentsChange(components: any[]) {
    this.recipeToAdd.components = components;
  }

  addRecipeComponent(){
    this.recipeToAdd.components.forEach(element => {
      
     // element.recipe = this.recipeToAdd // Recovery of the recipe updated with its new id
        this.componentService.addRecipeComponents(this.creatComponent(element)).subscribe({
          error: (err) => {
            console.log(err.error.message)
          }
        })
    });
  }

  creatComponent( element: RecipeComponent): RecipeComponent{
    const componentToAdd: RecipeComponent = {
      id: element.id,
      recipe: this.recipeToAdd,
    quantity: element.quantity,
    ingredient: element.ingredient, 
    unit: element.unit
    }

    return componentToAdd
  }
}
