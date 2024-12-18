import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ContestCategory } from '../models/contest-category';

// This service talks to the backend API
@Injectable({
  providedIn: 'root', // This means it is available everywhere in the app
})
export class ContestCategoryService {
  // API URL to communicate with the backend
  private apiUrl = 'http://localhost:8080/api/contest-categories';

  constructor(private http: HttpClient) {}

  // Get all contest categories from the backend
  getAllCategories(): Observable<ContestCategory[]> {
    return this.http.get<ContestCategory[]>(this.apiUrl).pipe(
      catchError(this.handleError) // Handle errors if something goes wrong
    );
  }

  // Create a new contest category
  createCategory(category: ContestCategory): Observable<ContestCategory> {
    return this.http.post<ContestCategory>(this.apiUrl, category).pipe(
      catchError(this.handleError) // Handle errors if the creation fails
    );
  }

  // Update an existing contest category by ID
  updateCategory(id: number, category: ContestCategory): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, category).pipe(
      catchError(this.handleError) // Handle errors if the update fails
    );
  }

  // Delete a contest category by ID
  deleteCategory(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError) // Handle errors if the deletion fails
    );
  }

  // Handle errors from the backend
  private handleError(error: HttpErrorResponse): Observable<never> {
    // Log the error in the console
    console.error(`Server-side error: ${error.message}`);
    // Return an observable with an error message
    return throwError('An error occurred. Please try again later.');
  }
}
