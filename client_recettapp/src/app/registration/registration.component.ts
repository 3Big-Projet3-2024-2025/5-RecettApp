import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Contest } from '../models/contest';
import { ContestCategory } from '../models/contest-category';
import { CommonModule } from '@angular/common';
import { User } from '../models/users';
import { KeycloakService } from 'keycloak-angular';
import {jwtDecode} from 'jwt-decode';
import { UsersService } from '../services/users.service';
import { PaypalService } from '../services/paypal.service';
import { Entry } from '../models/entry';

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

  entry: Entry = {id: 0, contest : undefined, users: undefined, status: "" };

  constructor(
    private keycloakService : KeycloakService,
    private userService : UsersService,
    private paypalService : PaypalService,
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

  async getUserByEmail(email: string): Promise<void> {
    const token = await this.keycloakService.getToken();
    const sub = this.userService.findByEmail(email, token).subscribe({
      next: (user) => {
        this.user = user;
        console.log(this.user);
        sub.unsubscribe();
      }, error: (err) => {
        console.log('Error getting user informations');
        sub.unsubscribe();
      }
    });
  }



  pay():void{
    this.entry.contest = this.contest;
    this.entry.users = this.user;
    this.entry.status = "process";
    const price = 20
    this.paypalService.payToRegister(price, this.entry).subscribe({
      next: (approvalUrl) => {
        console.log('Redirecting to PayPal URL:', approvalUrl);
        window.location.href = approvalUrl;
      },
      error: (err) => {
        console.error("PayPal Error:", err);
      }
    });
  }

}
