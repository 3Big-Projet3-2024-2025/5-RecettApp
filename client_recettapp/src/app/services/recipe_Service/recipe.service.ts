import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Recipe } from '../../models/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {


  constructor(private http: HttpClient) { }
  private Url = "http://localhost:8080/recipe";

  getAllRecipe(token: any): Observable<Recipe[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Recipe[]>(this.Url+'/all', {headers});
  }

  getRecipeById(id: number, token: any): Observable<Recipe> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Recipe>(`${this.Url}/${id}`, {headers});
  }

  addRecipe(recipe: Recipe, token: any): Observable<Recipe>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<Recipe>(this.Url,recipe, {headers});
  }

  deleteRecipe(id: number, token: any): Observable<Recipe> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete<Recipe>(`${this.Url}/${id}`, {headers});
  }


  getRecipeByIdContest(idContest: number, token: any): Observable<Recipe[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Recipe[]>(`${this.Url}/contest/${idContest}`, {headers});
  }

  getRecipesPaginated(keyword: string, page: number, size: number, token: any): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = {
      keyword: keyword || '',
      page: page.toString(),
      size: size.toString(),
    };
    return this.http.get<any>(this.Url, { params, headers });
  }

  getRecipesByUserMail(email: string, keyword: string,page: number, size: number, token: any): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = new HttpParams()
      .set('email', email)
      .set('page', page.toString())
      .set('keyword', keyword)
      .set('size', size.toString());
    return this.http.get<any>(`${this.Url}/usermail`, { params, headers });
  }

  anonymizeRecipe(id: number, token: any): Observable<void> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<void>(`${this.Url}/anonymize/${id}`, {headers});
  }
}
