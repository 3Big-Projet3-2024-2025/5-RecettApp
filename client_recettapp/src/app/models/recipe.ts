import { RecipeComponent } from "../models/recipe-component";
import { Contest } from "./contest";
import { Entry } from "./entry";
import { ImageData } from "./image-data";
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
    masked: boolean; 
    recipe_type?: RecipeType; 
    entry?: Entry; 
    components: RecipeComponent[];
    image: ImageData[];
}
