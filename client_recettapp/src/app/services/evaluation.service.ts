import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Evaluation } from '../models/evaluation';

@Injectable({
  providedIn: 'root'
})
export class EvaluationService {


  private baseUrl = 'http://localhost:8080/api/evaluations';
  constructor(private http: HttpClient) { }

  getAllEvaluations(): Observable<Evaluation[]> {
    return this.http.get<Evaluation[]>(this.baseUrl);
  }

  getEvaluationsByEntry(entryId: number): Observable<Evaluation[]> {
    return this.http.get<Evaluation[]>(`${this.baseUrl}/entry/${entryId}`);
  }

  getEvaluationsByRecipe(recipeId: number): Observable<Evaluation[]> {
    return this.http.get<Evaluation[]>(`${this.baseUrl}/recipe/${recipeId}`);
  }

  addEvaluation(evaluation: Evaluation): Observable<Evaluation> {
    return this.http.post<Evaluation>(this.baseUrl, evaluation);
  }

  deleteEvaluation(id: number, isAdmin: boolean): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}?isAdmin=${isAdmin}`);
  }
}
