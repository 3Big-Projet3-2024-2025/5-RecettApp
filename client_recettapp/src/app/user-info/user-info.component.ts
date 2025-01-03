import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import {jwtDecode} from 'jwt-decode';
import { UsersService } from '../services/users.service';
import { User } from '../models/users';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-info',
  standalone:true,
  imports:[FormsModule,CommonModule],
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
        console.log("Token reçu :", token); 
        const decodedToken: any = jwtDecode(token);
        const email = decodedToken.email; 
        console.log("Email décodé :", email);
        this.userName = decodedToken.preferred_username; 
        this.getUserByEmail(email); 
      })
      .catch(err => {
        console.error("Erreur lors de la récupération du token :", err);
      });
  }
  

  getUserByEmail(email: string): void {
    this.userService.findByEmail(email).subscribe({
      next: (user) => {
        console.log("Utilisateur récupéré :", user); 
        this.user = user;
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des informations utilisateur :", err);
      }
    });
  }
  
}
