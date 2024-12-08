import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { provideHttpClient } from '@angular/common/http';
import {AuthKeycloakService} from "./app/services/auth-keycloak.service";
import {APP_INITIALIZER} from "@angular/core";
import {KeycloakAngularModule} from "keycloak-angular";

bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes), provideHttpClient(),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      deps: [AuthKeycloakService],
      multi: true
    },
    KeycloakAngularModule],
}).catch((err) => console.error(err));

/**
 * A factory function that initializes the Keycloak authentication process.
 * This function is typically used for initializing Keycloak during the application's
 * startup, ensuring the authentication system is ready before the app is fully loaded.
 *
 * @param {AuthKeycloakService} authKeycloakService - An instance of the `AuthKeycloakService` which handles the
 * Keycloak authentication flow and configuration.
 *
 * @returns {() => Promise<void>} A function that, when invoked, initializes the Keycloak service.
 */
export function initializeKeycloak(authKeycloakService: AuthKeycloakService) {
  return () => authKeycloakService.init();
}
