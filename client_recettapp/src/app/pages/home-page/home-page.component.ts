import { Component } from '@angular/core';

import { RecipeService } from '../../services/recipe_Service/recipe.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Recipe } from '../../models/recipe';
import { FooterComponent } from '../../footer/footer.component';
import { NavBarComponent } from '../../nav-bar/nav-bar.component';


@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [FooterComponent],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {
}
