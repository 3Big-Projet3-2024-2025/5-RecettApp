import { Ingredient } from "./ingredient";
import { Recipe } from "./recipe";

export interface RecipeComponent {
    id: number; 
    recipe: Recipe; 
    quantity: number; 
    ingredient: Ingredient; 
    unit: string; 
}
