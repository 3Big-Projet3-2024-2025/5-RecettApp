import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Recipe } from '../Interface/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  

  constructor(private http: HttpClient) { }
  private Url = "http://localhost:8080/recipe/all";

  getAllRecipe(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.Url);
  }
}
