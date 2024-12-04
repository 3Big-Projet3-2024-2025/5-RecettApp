import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contest } from '../models/contest';


@Injectable({
  providedIn: 'root'
})
export class ContestService {
  private apiUrl = 'http://localhost:8080/contests';  

  constructor(private http: HttpClient) { }

  getAllContests(): Observable<Contest[]> {
    return this.http.get<Contest[]>(this.apiUrl);
  }

  addContest(contest: Contest): Observable<Contest> {
    return this.http.post<Contest>(this.apiUrl, contest);
  }
}
