import {KeycloakAuthGuard, KeycloakService} from "keycloak-angular";
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from "@angular/router";
import {UsersService} from "../services/users.service";
import {jwtDecode} from 'jwt-decode';
import {User} from "../models/users";

@Injectable({
  providedIn: 'root',
})
export class KeycloakGuard extends KeycloakAuthGuard {
  constructor(protected override router: Router, protected keycloakService: KeycloakService, protected userService: UsersService) {
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
        try {
          const token = await this.keycloakService.getToken();
          const decodedToken: any = jwtDecode(token);

          console.log('Decoded Token:', decodedToken);

          this.userService.findByEmail(decodedToken.email, token).subscribe( user => {},
            error => {
              if (error.status === 404) {
                let date = new Date();
                // Format this date to obtain ISO format "yyyy-MM-dd"
                let formattedDate = date.toISOString().split('T')[0];

                let user: User = {
                  id: 0,
                  firstName: decodedToken.given_name,
                  lastName: decodedToken.family_name,
                  email: decodedToken.email,
                  date_registration: formattedDate,
                  blocked: false
                };
                this.userService.save(user, token).subscribe( user => {},
                  error => {
                    console.error('Failed to save user', error);
                  });
              } else {
                console.log(error);
              }
            }
          )

        } catch (err) {
          console.error('Failed to load user attributes', err);
        }
        resolve(true);
      }
    });
  }
}
