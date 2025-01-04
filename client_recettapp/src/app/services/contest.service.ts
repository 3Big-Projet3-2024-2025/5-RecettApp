import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contest } from '../models/contest';
import { PaginatedResponse } from '../models/paginatedResponse';


@Injectable({
  providedIn: 'root'
})
export class ContestService {
  private apiUrl = 'http://localhost:8080/contests';  

  constructor(private http: HttpClient) { }

  getAllContests(page: number = 0, size: number = 10): Observable<PaginatedResponse<any>> {
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<PaginatedResponse<any>>(this.apiUrl, { params });
}

  addContest(contest: Contest): Observable<Contest> {
    return this.http.post<Contest>(this.apiUrl, contest);
  }

  updateContest(contest: Contest): Observable<Contest> {
    return this.http.put<Contest>(this.apiUrl, contest);
  }

  deleteContest(idContest : number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idContest}`);
  }
}
