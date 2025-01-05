import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contest } from '../models/contest';
import { PaginatedResponse } from '../models/paginatedResponse';


@Injectable({
  providedIn: 'root'
})
export class ContestService {
  private apiUrl = 'http://localhost:8080/contests';

  constructor(private http: HttpClient) { }

  getAllContests(page: number = 0, size: number = 10, token: any): Observable<PaginatedResponse<any>> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<PaginatedResponse<any>>(this.apiUrl, { params, headers });
}

  getAllAvailableContests(page: number = 0, size: number = 10, token: any): Observable<PaginatedResponse<any>> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<PaginatedResponse<any>>(`${this.apiUrl}/availableContests`, { params, headers });
}

  addContest(contest: Contest, token: any): Observable<Contest> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<Contest>(this.apiUrl, contest, {headers});
  }

  updateContest(contest: Contest, token: any): Observable<Contest> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<Contest>(this.apiUrl, contest, {headers});
  }

  deleteContest(idContest : number, token: any): Observable<void> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete<void>(`${this.apiUrl}/${idContest}`, {headers});
  }
}
