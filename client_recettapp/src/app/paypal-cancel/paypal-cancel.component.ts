import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-paypal-cancel',
  standalone: true,
  imports: [],
  templateUrl: './paypal-cancel.component.html',
  styleUrl: './paypal-cancel.component.css'
})
export class PaypalCancelComponent {
  countdown: number = 5;

  constructor(
    private router: Router
  ) { }

  ngOnInit():void{

    this.startCountdown();
    
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
