import { Entry } from "./entry";
import { Recipe } from "./recipe";
import { RecipeType } from "./recipe-type";
import { User } from "./users";

export interface Evaluation {
  id: number;
  rate: number;
  entry?: Entry;
  recipe?: Recipe;
}
