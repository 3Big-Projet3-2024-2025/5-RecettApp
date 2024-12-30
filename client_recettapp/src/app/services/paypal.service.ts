import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs';
import { PayPalPayment } from '../models/paypal-payment';

interface PayPalResponse {
  approvalUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaypalService {
  private apiUrl = 'http://localhost:8080/paypal';

  constructor(private http: HttpClient) { }


  payToRegister(amount: number): Observable<string> {
    console.log('Initiating PayPal payment for amount:', amount);
    return this.http.get<PayPalResponse>(`${this.apiUrl}/pay?total=${amount}`)
      .pipe(
        map(response => {
          console.log('Received PayPal response:', response);
          return response.approvalUrl;
        })
      );
  }

  /*
  Save succesful payment's data to DB
  */
  addPaypalPayment(payment : PayPalPayment): Observable<any>{
    return this.http.post<PayPalPayment>(`${this.apiUrl}`, payment);
  }

  /*
  Check if a paypal payment is valid
  */
  validatePayment(paymentId: string, payerId: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validate`, {
      params: { paymentId, payerId }
    });
  }
}
