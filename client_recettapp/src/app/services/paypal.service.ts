import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs';
import { PayPalPayment } from '../models/paypal-payment';
import { Entry } from '../models/entry';

interface PayPalResponse {
  approvalUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaypalService {
  private apiUrl = 'http://localhost:8080/paypal';

  constructor(private http: HttpClient) { }

  /*
  Proceed paypal payment and add entry
  */
  payToRegister(amount: number, entry : Entry, token: any): Observable<string> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    console.log('Initiating PayPal payment for amount:', amount);
    return this.http.post<PayPalResponse>(`${this.apiUrl}/pay?total=${amount}`, entry, {headers})
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
  addPaypalPayment(userId: number, response: string, token: any): Observable<any>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<PayPalPayment>(`${this.apiUrl}/response?userId=${userId}`, response, {headers});
  }

  /*
  Check if a paypal payment is valid
  */
  validatePayment(paymentId: string, payerId: string, uuid: string, token: any): Observable<boolean> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<boolean>(`${this.apiUrl}/validate`, {
      params: { uuid, paymentId, payerId },
      headers
    });
  }
}
