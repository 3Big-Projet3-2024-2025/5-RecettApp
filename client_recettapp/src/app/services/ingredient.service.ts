import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  private apiUrl = 'http://localhost:8080/ingredients';

  constructor(private http: HttpClient) {}

  searchIngredients(term: string, token: any): Observable<any[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any[]>(`${this.apiUrl}/search?term=${term}`, {headers});
  }
}
