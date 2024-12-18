import { Injectable } from '@angular/core';
import {KeycloakService} from "keycloak-angular";

@Injectable({
  providedIn: 'root'
})
export class AuthKeycloakService {

  constructor(private keycloakService: KeycloakService) {}

  /**
   * Initializes the Keycloak service with the required configuration.
   * Ensures that the user is authenticated upon initialization.
   *
   * @returns {Promise<void>} A promise that resolves when initialization is complete.
   */
  async init() {
    await this.keycloakService.init({
      config: {
        url: 'http://localhost:8081',
        realm: 'recettapp',
        clientId: 'frontend_recettapp'
      },
      initOptions: {
        onLoad: 'check-sso',
        checkLoginIframe: false,
        enableLogging: true,
        pkceMethod: 'S256',
        flow: 'standard',
      }
    });
  }

  /**
   * Checks if the user is authenticated.
   *
   * @returns {Promise<boolean>} A promise that resolves to true if the user is authenticated, false otherwise.
   */
  isAuthenticated() {
    return this.keycloakService.isLoggedIn();
  }

  /**
   * Logs the user out by invoking Keycloak's logout method.
   *
   * @returns {Promise<void>} A promise that resolves when the logout process is complete.
   */
  logout() {
    return this.keycloakService.logout();
  }

  /**
   * Retrieves the access token for the current authenticated user.
   *
   * @returns {Promise<string>} A promise that resolves to the access token string.
   */
  getToken() {
    return this.keycloakService.getToken();
  }

  /**
   * Redirects the user to the Keycloak login page.
   *
   * <p>This method uses the {@code KeycloakService} to initiate the login process by redirecting the user
   * to the Keycloak authentication page. This is typically called when the user needs to authenticate.</p>
   */
  redirectToLogin() {
    this.keycloakService.login();
  }
}
