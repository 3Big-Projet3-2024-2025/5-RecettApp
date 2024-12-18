import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RecipeComponent } from '../../models/recipe-component';

@Injectable({
  providedIn: 'root'
})
export class RecipeComponentService {


  constructor(private http: HttpClient) { }
  private Url = "http://localhost:8080/recipe-components";

  getRecipeComponentsByIdRecipe(idRecipe: number): Observable<RecipeComponent[]> {
    return this.http.get<RecipeComponent[]>(`${this.Url}/recipe/${idRecipe}`);
  }

  addRecipeComponents(recipeComponents: RecipeComponent): Observable<RecipeComponent>{
    return this.http.post<RecipeComponent>(this.Url,recipeComponents);
  }
}
