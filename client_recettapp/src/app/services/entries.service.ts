import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Entry } from '../models/entry';

@Injectable({
  providedIn: 'root'
})
export class EntriesService {
  private apiUrl = "http://localhost:8080/entries";

  constructor(private http: HttpClient) { }

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

  getEntry(idEntry: number): Observable<Entry> {
    return this.http.get<Entry>(`${this.apiUrl}/${idEntry}`);
  }
}
