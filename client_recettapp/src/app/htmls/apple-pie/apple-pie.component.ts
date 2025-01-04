import { Component } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-apple-pie',
  standalone: true,
  imports: [RouterModule,RouterLink],
  templateUrl: './apple-pie.component.html',
  styleUrl: './apple-pie.component.css'
})
export class ApplePieComponent {

}
