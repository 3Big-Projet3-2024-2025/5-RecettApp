import { RecipeComponent } from "../models/recipe-component";
import { Contest } from "./contest";
import { RecipeType } from "./recipe-type";

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
    recipe_type?: RecipeType; 
    contest?: Contest; 
    components: RecipeComponent[];
    image: ImageData[];
}
