import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Evaluation } from '../models/evaluation';

@Injectable({
  providedIn: 'root'
})
export class EvaluationService {


  private baseUrl = 'http://localhost:8080/api/evaluations';
  constructor(private http: HttpClient) { }

  getAllEvaluations(token: any): Observable<Evaluation[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Evaluation[]>(this.baseUrl, {headers});
  }

  getEvaluationsByEntry(entryId: number, token: any): Observable<Evaluation[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Evaluation[]>(`${this.baseUrl}/entry/${entryId}`, {headers});
  }

  getEvaluationsByRecipe(recipeId: number, token: any): Observable<Evaluation[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Evaluation[]>(`${this.baseUrl}/recipe/${recipeId}`, {headers});
  }

  addEvaluation(evaluation: Evaluation, token: any): Observable<Evaluation> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<Evaluation>(this.baseUrl, evaluation, {headers});
  }


  deleteEvaluation(id: number, isAdmin: boolean, token: any): Observable<void> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete<void>(`${this.baseUrl}/${id}?isAdmin=${isAdmin}`, {headers});
  }
}
