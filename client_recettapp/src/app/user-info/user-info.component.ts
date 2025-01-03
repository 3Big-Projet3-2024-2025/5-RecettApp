import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import {jwtDecode} from 'jwt-decode';
import { UsersService } from '../services/users.service';
import { User } from '../models/users';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  user!: User; 
  userName!: string; 

  constructor(
    private keycloakService: KeycloakService,
    private userService: UsersService
  ) {}

  ngOnInit(): void {
    this.getUserDetail();
  }

  
  getUserDetail(): void {
    this.keycloakService.getToken()
      .then(token => {
        const decodedToken: any = jwtDecode(token);
        const email = decodedToken.email; 
        this.userName = decodedToken.preferred_username; 
      })
      .catch(err => {
        console.error("Erreur lors de la récupération des informations utilisateur :", err);
      });
  }

  getUserByEmail(email: string): void {
    this.userService.findByEmail(email).subscribe({
      next: (user) => {
        this.user = user; // Affecter les données utilisateur
        console.log("Données utilisateur :", this.user);
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des informations utilisateur :", err);
      }
    });
  }
}
