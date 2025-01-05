import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
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

  getAllEntries(token: any): Observable<Entry[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Entry[]>(this.apiUrl, {headers});
  }

  addEntry(entry: Entry, token: any): Observable<Entry> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<Entry>(this.apiUrl, entry, {headers});
  }

  updateEntry(entry: Entry, token: any): Observable<Entry> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<Entry>(this.apiUrl, entry, {headers});
  }

  deleteEntry(idEntry : number, token: any): Observable<void> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete<void>(`${this.apiUrl}/${idEntry}`, {headers});
  }

  deleteEntryUuid(uuid : string, token: any): Observable<void> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete<void>(`${this.apiUrl}/cancelEntry/${uuid}`, {headers});
  }

  getEntry(idEntry: number, token: any): Observable<Entry> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Entry>(`${this.apiUrl}/${idEntry}`, {headers});
  }

  getEntryByUserMailAndIdContest(idContest: number, token: any):Observable<Entry>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return from(this.getUserMail()).pipe(
      switchMap(userMail => {
        const url = `${this.apiUrl}/entry?contestId=${idContest}&userMail=${userMail}`;
        return this.http.get<Entry>(url, {headers});
      })
    );

  }
  async getUserMail(){
     const token = await this.authService.getToken();
    const decodedToken: any = jwtDecode(token);
    return decodedToken.email;
  }
}
