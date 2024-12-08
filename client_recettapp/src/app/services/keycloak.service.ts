import { Injectable } from '@angular/core';
import Keycloak from "keycloak-js/lib/keycloak";

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private keycloakInstance!: Keycloak;

  constructor() {
    this.initKeycloak();
  }

  /**
   * Initialize Keycloak instance and configure it
   */
  private initKeycloak(): void {
    this.keycloakInstance = new Keycloak({
      url: 'http://localhost:8081',
      realm: 'recettapp',
      clientId: 'frontend_recettapp'
    });
  }

  /**
   * Get the current Keycloak instance
   */
  public getKeycloakInstance(): Keycloak {
    if (!this.keycloakInstance){
      this.initKeycloak();
    }
    return this.keycloakInstance;
  }
}
