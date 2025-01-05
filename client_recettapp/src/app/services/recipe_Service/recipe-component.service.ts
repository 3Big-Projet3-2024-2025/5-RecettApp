import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RecipeComponent } from '../../models/recipe-component';

@Injectable({
  providedIn: 'root'
})
export class RecipeComponentService {


  constructor(private http: HttpClient) { }
  private Url = "http://localhost:8080/recipe-components";

  getRecipeComponentsByIdRecipe(idRecipe: number, token: any): Observable<RecipeComponent[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<RecipeComponent[]>(`${this.Url}/recipe/${idRecipe}`, {headers});
  }

  addRecipeComponents(recipeComponents: RecipeComponent, token: any): Observable<RecipeComponent>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<RecipeComponent>(this.Url,recipeComponents, {headers});
  }
}
