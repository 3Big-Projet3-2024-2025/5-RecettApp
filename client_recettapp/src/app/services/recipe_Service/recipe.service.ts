import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Recipe } from '../../models/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  

  constructor(private http: HttpClient) { }
  private Url = "http://localhost:8080/recipe";

  getAllRecipe(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.Url+'/all');
  } 
  
  getRecipeById(id: number): Observable<Recipe> {
    return this.http.get<Recipe>(`${this.Url}/${id}`);
  }

  addRecipe(recipe: Recipe): Observable<Recipe>{
    return this.http.post<Recipe>(this.Url,recipe);
  }

  deleteRecipe(id: number): Observable<Recipe> {
    return this.http.delete<Recipe>(`${this.Url}/${id}`);
  }

  
  getRecipeByIdContest(idContest: number): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(`${this.Url}/contest/${idContest}`); 
  } 
}
