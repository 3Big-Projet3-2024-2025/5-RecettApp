import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
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
  getAllCategories(token: any): Observable<ContestCategory[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<ContestCategory[]>(this.apiUrl, {headers}).pipe(
      catchError(this.handleError) // Handle errors if something goes wrong
    );
  }

  // Create a new contest category
  createCategory(category: ContestCategory, token: any): Observable<ContestCategory> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<ContestCategory>(this.apiUrl, category, {headers}).pipe(
      catchError(this.handleError) // Handle errors if the creation fails
    );
  }

  // Update an existing contest category by ID
  updateCategory(id: number, category: ContestCategory, token: any): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put(`${this.apiUrl}/${id}`, category, {headers}).pipe(
      catchError(this.handleError) // Handle errors if the update fails
    );
  }

  // Delete a contest category by ID
  deleteCategory(id: number, token: any): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete(`${this.apiUrl}/${id}`, {headers}).pipe(
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
