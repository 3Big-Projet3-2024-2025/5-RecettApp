import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Recipe } from '../models/recipe';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RecipeService } from '../services/recipe_Service/recipe.service';

@Component({
  selector: 'app-add-recipe',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './add-recipe.component.html',
  styleUrl: './add-recipe.component.css'
})
export class AddRecipeComponent {

  constructor(private route: ActivatedRoute,private recipeService: RecipeService){}
  ngOnInit(): void {
    const contestId = this.route.snapshot.paramMap.get('idConstest');
    if (contestId != null) {
      this.recipeToAdd.contest_id = +contestId
    }
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
    contest_id: 0,
    components: [],
    recipe_type_id: null
  };



  onSubmit(): void {
      this.recipeService.addRecipe(this.recipeToAdd).subscribe(
        {
          next(value) {
            console.log("succes add")
          },
          error(err) {
            console.log(err.error.message)
          },
        }
      )
  }
}
