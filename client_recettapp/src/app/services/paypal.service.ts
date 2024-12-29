import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs';

interface PayPalResponse {
  approvalUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaypalService {
  private apiUrl = 'http://localhost:8080/paypal/pay';

  constructor(private http: HttpClient) { }


  payToRegister(amount: number): Observable<string> {
    console.log('Initiating PayPal payment for amount:', amount);
    return this.http.get<PayPalResponse>(`${this.apiUrl}?total=${amount}`)
      .pipe(
        map(response => {
          console.log('Received PayPal response:', response);
          return response.approvalUrl;
        })
      );
  }
}
