import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecipeType } from '../models/recipe-type';

@Injectable({
  providedIn: 'root',
})
export class RecipeTypeService {
  private apiUrl = 'http://localhost:8080/api/recipe-types';

  constructor(private http: HttpClient) {}

  // Obtenir tous les types de recettes
  getAllRecipeTypes(): Observable<RecipeType[]> {
    return this.http.get<RecipeType[]>(this.apiUrl);
  }

  // Obtenir un type de recette par son ID
  getRecipeTypeById(id: number): Observable<RecipeType> {
    return this.http.get<RecipeType>(`${this.apiUrl}/${id}`);
  }

  // Rechercher un type de recette par son nom
  searchRecipeTypes(name: string): Observable<RecipeType[]> {
    return this.http.get<RecipeType[]>(`${this.apiUrl}/search?name=${name}`);
  }

  // Créer un nouveau type de recette
  createRecipeType(recipeType: RecipeType): Observable<RecipeType> {
    return this.http.post<RecipeType>(this.apiUrl, recipeType);
  }

  // Mettre à jour un type de recette existant
  updateRecipeType(id: number, recipeType: RecipeType): Observable<RecipeType> {
    return this.http.put<RecipeType>(`${this.apiUrl}/${id}`, recipeType);
  }

  // Supprimer un type de recette par son ID
  deleteRecipeType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
