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

    console.log('token', token)
    return this.http.get<User[]>(`${this.baseUrl}`, {headers});
  }

  findById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  findByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/email/${email}`);
  }

  save(user: User): Observable<User> {
    if (user.id) {
      return this.http.put<User>(`${this.baseUrl}/${user.id}`, user);
    } else {
      return this.http.post<User>(`${this.baseUrl}`, user);
    }
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  authenticate(email: string, password: string): Observable<any> {
    const credentials = { email, password };
    return this.http.post(`${this.baseUrl}/authenticate`, credentials);
  }

  unblockUser(email: string): Observable<any>{
    return this.http.post(`${this.baseUrl}/${email}/unblock`, email)
  }

  blockUser(email: string): Observable<any>{
    return this.http.post(`${this.baseUrl}/${email}/block`, email)
  }
}
