import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RecipeType } from '../models/recipe-type';

@Injectable({
  providedIn: 'root',
})
export class RecipeTypeService {
  private apiUrl = 'http://localhost:8080/api/recipe-types'; 

  constructor(private http: HttpClient) {}

  // Fetch all recipe types from the server
  getAllRecipeTypes(): Observable<RecipeType[]> {
    return this.http.get<RecipeType[]>(this.apiUrl).pipe(
      catchError(this.handleError) // Handle errors if the request fails
    );
  }

  // Create a new recipe type and send it to the server
  createRecipeType(recipeType: RecipeType): Observable<RecipeType> {
    return this.http.post<RecipeType>(this.apiUrl, recipeType).pipe(
      catchError(this.handleError) 
    );
  }

  // Update an existing recipe type by ID
  updateRecipeType(id: number, recipeType: RecipeType): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, recipeType).pipe(
      catchError(this.handleError) 
    );
  }

  // Delete a recipe type by ID
  deleteRecipeType(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError) 
    );
  }

  // Handle any errors from the server
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error(`Server-side error: ${error.message}`); // Log the error for debugging
    return throwError('An error occurred. Please try again later.'); 
  }
}
