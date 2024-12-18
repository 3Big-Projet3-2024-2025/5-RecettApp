import {KeycloakAuthGuard, KeycloakService} from "keycloak-angular";
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from "@angular/router";

@Injectable({
  providedIn: 'root',
})
export class KeycloakGuard extends KeycloakAuthGuard {
  constructor(protected override router: Router, protected keycloakService: KeycloakService) {
    super(router, keycloakService);
  }

  isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {
    return new Promise(async (resolve) => {
      if (!this.authenticated) {
        await this.keycloakService.login(); // Redirect to keycloak login page
        resolve(false);
      } else {
        resolve(true);
      }
    });
  }
}
