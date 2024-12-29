import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Contest } from '../models/contest';
import { ContestCategory } from '../models/contest-category';
import { CommonModule } from '@angular/common';
import { User } from '../models/users';
import { KeycloakService } from 'keycloak-angular';
import {jwtDecode} from 'jwt-decode';
import { UsersService } from '../services/users.service';

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
  userName!: string;

  constructor(
    private keycloakService : KeycloakService,
    private userService : UsersService
  ) {}

  ngOnInit() {
    console.log('Received contest:', this.contest);
    this.getUserDetail();
  }

  getUserDetail():void{
    this.keycloakService.getToken()
      .then(token => {
        const decodedToken:any = jwtDecode(token);
        const email = decodedToken.email;
        this.userName = decodedToken.preferred_username;
        // get User info
        this.getUserByEmail(email);
      })
      .catch(err => {
        console.log('Error getting user informations');
      })
  }

  onCancel():void{
    this.cancelRegistration.emit();
  }

  getUserByEmail(email : string):void{
    const sub = this.userService.findByEmail(email).subscribe({
      next: (user) => {
        this.user = user;
        console.log(this.user);
        sub.unsubscribe();
      }, error : (err) => {
        console.log('Error getting user informations');
        sub.unsubscribe();
      }
    });
  }

}
