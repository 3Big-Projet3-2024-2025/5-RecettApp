import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { UsersComponent } from './users/users.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,ContestCategoryComponent,UsersComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'client_recettapp';
}
