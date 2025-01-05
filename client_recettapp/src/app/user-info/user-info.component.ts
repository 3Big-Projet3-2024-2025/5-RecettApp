import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { jwtDecode } from 'jwt-decode';
import { UsersService } from '../services/users.service';
import { User } from '../models/users';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-info',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  user!: User; // Holds the user data
  userName!: string; // Holds the username from the token

  constructor(
    private keycloakService: KeycloakService, // For authentication
    private userService: UsersService // To fetch user details
  ) {}

  ngOnInit(): void {
    this.getUserDetail(); // Get user details when the page loads
  }

  // Get user data from the token
  getUserDetail(): void {
    this.keycloakService.getToken()
      .then(token => {
        const decodedToken: any = jwtDecode(token); // Decode the token
        const email = decodedToken.email; // Get the email
        this.userName = decodedToken.preferred_username; // Get the username
        this.getUserByEmail(email); // Fetch user info using email
      })
      .catch(err => {
        console.error("Cannot get token:", err); // Error if token fails
      });
  }

  // Get user info from the backend
  async getUserByEmail(email: string): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.userService.findByEmail(email, token).subscribe({
      next: (user) => {
        this.user = user; // Save the user data
      },
      error: (err) => {
        console.error("Cannot get user info:", err); // Error if backend fails
      }
    });
  }
}
