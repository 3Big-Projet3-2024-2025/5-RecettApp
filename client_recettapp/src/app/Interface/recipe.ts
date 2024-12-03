import { RecipeComponent } from "./recipe-component";

export interface Recipe {
    id: number; 
    title: string; 
    description: string; 
    category: string;
    preparationTime: number; 
    cookingTime: number; 
    servings: number; 
    difficultyLevel: string; 
    instructions: string; 
    photoUrl?: string;
    approved: boolean; 
    recipeType: string | null; 
    contestId: number; 
    components: RecipeComponent[];
}
