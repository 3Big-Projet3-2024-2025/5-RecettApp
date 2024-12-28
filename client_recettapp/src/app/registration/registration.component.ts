import { Component, Input } from '@angular/core';
import { Contest } from '../models/contest';
import { ContestCategory } from '../models/contest-category';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  @Input() contest!: Contest;

  ngOnInit() {
    console.log('Received contest:', this.contest);
  }

}
