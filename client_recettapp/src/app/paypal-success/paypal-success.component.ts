import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PayPalPayment } from '../models/paypal-payment';
import { PaypalService } from '../services/paypal.service';
import { Router } from '@angular/router';
import {UsersService} from "../services/users.service";
import {User} from "../models/users";
import {KeycloakService} from "keycloak-angular";


@Component({
  selector: 'app-paypal-success',
  standalone: true,
  imports: [],
  templateUrl: './paypal-success.component.html',
  styleUrl: './paypal-success.component.css'
})
export class PaypalSuccessComponent {

  paymentData!: PayPalPayment;
  paymentStatus!: string;
  countdown: number = 5;
  user: User = {
    id: 0,
    firstName: '',
    lastName: '',
    email: '',
    date_registration: '',
    blocked: false
  }

  constructor(
    private route: ActivatedRoute,
    private paypalService: PaypalService,
    private router: Router,
    private keycloakService: KeycloakService,
    private userService: UsersService
  ){}


  ngOnInit():void{
    const uuid = this.route.snapshot.queryParamMap.get('uuid') || '';
    const paymentId = this.route.snapshot.queryParamMap.get('paymentId') || '';
    const token = this.route.snapshot.queryParamMap.get('token') || '';
    const payerId = this.route.snapshot.queryParamMap.get('PayerID') || '';

    this.setPaypalPaymentData(paymentId, token, payerId);
    this.validatePayment(uuid, paymentId,payerId);
    console.log('PayPal Payment Data:', this.paymentData);
    this.addPaypalPayment();
    this.startCountdown();

  }



  setPaypalPaymentData(paymentId : string, token : string, payerId : string){
    this.paymentData = {
      paymentId : paymentId,
      token : token,
      payerId : payerId,
    };
  }

  validatePayment(uuid : string, paymentId : string,payerId : string){
    if (paymentId && payerId) {
      this.paypalService.validatePayment(paymentId, payerId, uuid).subscribe({
        next: (isSuccessful) => {
          this.paymentStatus = isSuccessful ? 'Payment Successful' : 'Payment Failed';

          if(isSuccessful){
            // check if payment is not already saved then save the payment
          } else{
            this.paymentStatus = 'This Payment was successful and already happened'
          }
        },
        error: (err) => {
          console.error('Payment validation error:', err);
          this.paymentStatus = 'Payment Validation Error';
        }
      });
    }
  }

  startCountdown(): void {
    const interval = setInterval(() => {
      if (this.countdown > 0) {
        this.countdown--;
      } else {
        clearInterval(interval);
        this.redirectToHome();
      }
    }, 1000);
  }

  redirectToHome(): void {
    this.router.navigate(['/home']);
  }

  addPaypalPayment(): void{
    let userToken = this.keycloakService.getKeycloakInstance().tokenParsed;
    if (userToken && userToken['email']) {
      this.userService.findByEmail(userToken['email']).subscribe(({
        next: (user) => {
          this.user = user;
          let response = `{ 'paymentId': ${this.paymentData.paymentId}, 'token' : ${this.paymentData.token}, 'payerId' : ${this.paymentData.payerId} }`
          this.paypalService.addPaypalPayment(this.user.id, response).subscribe(({
            next: ()=> {},
            error: (error) => {
              console.error('Error to add payment:', error.error.error);
            }
          }));
        }, error: (error) => {
          console.error('Error to get user:', error.error.error);
        }
      }));
    }
  }



}
