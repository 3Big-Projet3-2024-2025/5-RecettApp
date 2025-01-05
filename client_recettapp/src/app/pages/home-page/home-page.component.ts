import { Component } from '@angular/core';

import { RecipeService } from '../../services/recipe_Service/recipe.service';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthKeycloakService } from '../../services/auth-keycloak.service';
import { KeycloakService } from 'keycloak-angular';
import { Recipe } from '../../models/recipe';
import { FooterComponent } from '../../footer/footer.component';
import { NavBarComponent } from '../../nav-bar/nav-bar.component';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [FooterComponent,RouterLink,CommonModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})

export class HomePageComponent {
  constructor(private authService : AuthKeycloakService, private keycloakService: KeycloakService){}
  isAuhtentificated= false;
  ngOnInit(){
    this.isAuhtentificated = this.authService.isAuthenticated()
}

logOut(){
 this.authService.logout();
}

logIn(){
 this.keycloakService.login()
}
}
