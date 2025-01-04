import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private keycloakService: KeycloakService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    try {
      // Ensure the user is authenticated
      const isAuthenticated = this.keycloakService.isLoggedIn();

      if (!isAuthenticated) {
        // Redirect to the login page if not authenticated
        await this.keycloakService.login();
        return false;
      }

      // Load the user roles
      const userRoles = this.keycloakService.getUserRoles();

      // Check if the user has the 'admin' role
      if (userRoles.includes('admin')) {
        return true;
      } else {
        // Redirect to an unauthorized page if the user is not an admin
        await this.router.navigate(['/not-authorized']);
        return false;
      }
    } catch (error) {
      console.error('Error in AdminGuard:', error);
      await this.router.navigate(['/**']);
      return false;
    }
  }
}
