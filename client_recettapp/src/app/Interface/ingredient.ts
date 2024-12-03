import { RecipeComponent } from "./recipe-component";

export interface Ingredient {
    id: number; 
    alimentSpecifyGroupCode: number; 
    alimentGroupCode: number; 
    alimentGroupName: string; 
    alimentName: string;
    alimentSpecifyGroupName: string; 
    recipeComponent?: RecipeComponent[];
  }

