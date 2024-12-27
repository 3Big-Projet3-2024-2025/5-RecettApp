import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';

import { RecipeTypeComponent } from './recipe-type/recipe-type.component';

import { UsersComponent } from './users/users.component';
import { NavBarComponent } from "./nav-bar/nav-bar.component";

@Component({
  selector: 'app-root',
  standalone: true,

  imports: [RouterOutlet, NavBarComponent],

  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'client_recettapp';
}
