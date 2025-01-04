import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { from, Observable, switchMap } from 'rxjs';
import { Entry } from '../models/entry';
import { KeycloakService } from 'keycloak-angular';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class EntriesService {
  private apiUrl = "http://localhost:8080/entries";

  constructor(private http: HttpClient,private authService: KeycloakService) { }

  getAllEntries(): Observable<Entry[]> {
    return this.http.get<Entry[]>(this.apiUrl);
  }

  addEntry(entry: Entry): Observable<Entry> {
    return this.http.post<Entry>(this.apiUrl, entry);
  }

  updateEntry(entry: Entry): Observable<Entry> {
    return this.http.put<Entry>(this.apiUrl, entry);
  }

  deleteEntry(idEntry : number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idEntry}`);
  }

  deleteEntryUuid(uuid : string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cancelEntry/${uuid}`);
  }

  getEntry(idEntry: number): Observable<Entry> {
    return this.http.get<Entry>(`${this.apiUrl}/${idEntry}`);
  }

  getEntryByUserMailAndIdContest(idContest: number):Observable<Entry>{
    return from(this.getUserMail()).pipe(
      switchMap(userMail => {
        const url = `${this.apiUrl}/entry?contestId=${idContest}&userMail=${userMail}`;
        return this.http.get<Entry>(url);
      })
    );
        
  }
  async getUserMail(){
     const token = await this.authService.getToken();
    const decodedToken: any = jwtDecode(token);
    return decodedToken.email;
  }
}
