import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { User } from '../models/users';
import {emit} from "@angular-devkit/build-angular/src/tools/esbuild/angular/compilation/parallel-worker";
import {KeycloakService} from "keycloak-angular";



@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private baseUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient, private keycloakService: KeycloakService) {}

  findAll(token: any): Observable<User[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    console.log('token', token);
    return this.http.get<User[]>(`${this.baseUrl}`, {headers});
  }

  findById(id: number, token: any): Observable<User> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User>(`${this.baseUrl}/${id}`, {headers});
  }

  findByEmail(email: string, token: any): Observable<User> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User>(`${this.baseUrl}/email/${email}`, {headers});
  }

  save(user: User, token: any): Observable<User> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    if (user.id) {
      return this.http.put<User>(`${this.baseUrl}/${user.id}`, user, {headers});
    } else {
      return this.http.post<User>(`${this.baseUrl}`, user, {headers});
    }
  }

  delete(id: number, token: any): Observable<void> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete<void>(`${this.baseUrl}/${id}`, {headers});
  }

  unblockUser(email: string, token: any): Observable<any>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post(`${this.baseUrl}/${email}/unblock`, email, {headers})
  }

  blockUser(email: string, token: any): Observable<any>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post(`${this.baseUrl}/${email}/block`, email, {headers})
  }
}
