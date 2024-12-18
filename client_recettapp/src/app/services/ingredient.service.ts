import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  private apiUrl = 'http://localhost:8080/ingredients'; 

  constructor(private http: HttpClient) {}

  searchIngredients(term: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search?term=${term}`);
  }
}
