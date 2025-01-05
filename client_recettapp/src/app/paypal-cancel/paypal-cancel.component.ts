import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EntriesService } from '../services/entries.service';
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-paypal-cancel',
  standalone: true,
  imports: [],
  templateUrl: './paypal-cancel.component.html',
  styleUrl: './paypal-cancel.component.css'
})
export class PaypalCancelComponent {
  countdown: number = 5;
  msg!: string;
  err!: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private entryService : EntriesService,
    private keycloakService: KeycloakService
  ) { }

  ngOnInit():void{
    const uuid: string = this.route.snapshot.queryParamMap.get('uuid') || '';
    this.startCountdown();
    this.deleteUuid(uuid);

  }

  async deleteUuid(uuid: string): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.entryService.deleteEntryUuid(uuid, token).subscribe({
      next: () => {
        this.msg = "Cancel the registration to the contest";
        this.err = false;
      },
      error: (err) => {
        console.error('Backend error:', err);
        this.msg = err.error?.message || 'An unknown error occurred';
        this.err = true;
      }
    });
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
