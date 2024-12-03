import { RecipeComponent } from "../models/recipe-component";

export interface Recipe {
    id: number; 
    title: string; 
    description: string; 
    category: string;
    preparation_time: number; 
    cooking_time: number; 
    servings: number; 
    difficulty_level: string; 
    instructions: string; 
    photo_url?: string;
    approved: boolean; 
    recipe_type_id: string | null; 
    contest_id: number; 
    components: RecipeComponent[];
}
