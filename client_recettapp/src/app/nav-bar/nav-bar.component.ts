import { Component } from '@angular/core';
import { AuthKeycloakService } from '../services/auth-keycloak.service';
import { KeycloakService } from 'keycloak-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent {
  constructor(private authService : AuthKeycloakService, private keycloakService: KeycloakService){}
  isAuhtentificated= false;

  ngOnInit(){
       this.isAuhtentificated = this.authService.isAuthenticated()
       this.checkAdmin();
  }

  logOut(){
    this.authService.logout();
  }

  logIn(){
    this.keycloakService.login()
  }

  checkAdmin():boolean{
    const userRoles = this.keycloakService.getUserRoles();

    // Check if the user has the 'admin' role
    if (userRoles.includes('admin')) {
      return true;
    } else {
      return false
    }
  }
}
