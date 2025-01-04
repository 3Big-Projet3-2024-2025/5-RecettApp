import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PayPalPayment } from '../models/paypal-payment';
import { PaypalService } from '../services/paypal.service';
import { Router } from '@angular/router';


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


  constructor(
    private route: ActivatedRoute,
    private paypalService: PaypalService,
    private router: Router
  ){}
  

  ngOnInit():void{
    const uuid = this.route.snapshot.queryParamMap.get('uuid') || '';
    const paymentId = this.route.snapshot.queryParamMap.get('paymentId') || '';
    const token = this.route.snapshot.queryParamMap.get('token') || '';
    const payerId = this.route.snapshot.queryParamMap.get('PayerID') || '';

    this.setPaypalPaymentData(paymentId, token, payerId);
    this.validatePayment(uuid, paymentId,payerId);
    console.log('PayPal Payment Data:', this.paymentData);
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





}
