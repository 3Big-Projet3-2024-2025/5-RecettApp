import { Recipe } from "./recipe";

export interface ImageData {
    id: number;
  name: string;
  type: string;
  imageData: string;
  recipe: Recipe; 
}
