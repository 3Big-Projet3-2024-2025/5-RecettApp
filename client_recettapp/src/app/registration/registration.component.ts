import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Contest } from '../models/contest';
import { ContestCategory } from '../models/contest-category';
import { CommonModule } from '@angular/common';
import { User } from '../models/users';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  @Input() contest!: Contest;
  @Output() cancelRegistration = new EventEmitter<void>();


  user!: User;

  ngOnInit() {
    console.log('Received contest:', this.contest);
  }

  getUserDetail():void{

  }

  onCancel():void{
    this.cancelRegistration.emit();
  }

}
