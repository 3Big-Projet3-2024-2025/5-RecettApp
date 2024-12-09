import { RecipeType } from "./recipe-type";
import { User } from "./users";

export interface Evaluation {
  id?: number;
 // entry: Entry;
  rate: number;
  user: User;
  recipe: RecipeType;
}
