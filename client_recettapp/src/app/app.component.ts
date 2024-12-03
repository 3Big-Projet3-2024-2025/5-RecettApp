import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RecipeComponent } from './recipe/recipe.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RecipeComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'client_recettapp';
}
