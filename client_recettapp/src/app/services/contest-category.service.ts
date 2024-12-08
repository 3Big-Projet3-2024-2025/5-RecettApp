import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ContestCategory } from '../models/contest-category';

@Injectable({
  providedIn: 'root',
})
export class ContestCategoryService {
  private apiUrl = 'http://localhost:8080/api/contest-categories';

  constructor(private http: HttpClient) {}

  getAllCategories(): Observable<ContestCategory[]> {
    return this.http.get<ContestCategory[]>(this.apiUrl);
  }

  getCategoryById(id: number): Observable<ContestCategory> {
    return this.http.get<ContestCategory>(`${this.apiUrl}/${id}`);
  }

  createCategory(category: ContestCategory): Observable<ContestCategory> {
    return this.http.post<ContestCategory>(this.apiUrl, category);
  }

  updateCategory(id: number, category: ContestCategory): Observable<ContestCategory> {
    return this.http.put<ContestCategory>(`${this.apiUrl}/${id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
