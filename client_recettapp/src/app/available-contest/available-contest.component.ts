import { Component } from '@angular/core';
import { ContestService } from '../services/contest.service';
import { Contest } from '../models/contest';
import { KeycloakService } from 'keycloak-angular';
import { Router } from '@angular/router';
import { RegistrationComponent } from '../registration/registration.component';
import { CommonModule } from '@angular/common';
import { EntriesService } from '../services/entries.service';

@Component({
  selector: 'app-available-contest',
  standalone: true,
  imports: [CommonModule, RegistrationComponent],
  templateUrl: './available-contest.component.html',
  styleUrls: ['./available-contest.component.css'],
})
export class AvailableContestComponent {
  contests: Contest[] = [];
  contestAccess: { [id: number]: boolean } = {};
  selectedContest?: Contest;
  showRegistration = false;
  totalPages: number = 0;
  currentPage: number = 0;

  constructor(
    private contestService: ContestService,
    private keycloakService: KeycloakService,
    private entryService: EntriesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getAllContests(this.currentPage, 5);
  }

  async getAllContests(page: number = 0, size: number): Promise<void> {
    const token = await this.keycloakService.getToken();
    const sub = this.contestService.getAllAvailableContests(page, size, token).subscribe({
      next: (data) => {
        this.contests = data.content;
        this.totalPages = data.totalPages;
        this.checkAccessForContests();
        sub.unsubscribe();
      },
      error: (err) => {
        console.error('ERROR GETALLCONTESTS', err);
        sub.unsubscribe();
      },
    });
  }

  async checkAccessForContests(): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.contests.forEach((contest) => {
      this.entryService.getEntryByUserMailAndIdContest(contest.id!, token).subscribe({
        next: (entry) => {
          console.log("Entry : ", entry)
          if (entry) {
              if (contest.id) { 
              this.contestAccess[contest.id] = entry?.status === 'registered';
            }
            else {
              if (contest.id) { 
              this.contestAccess[contest.id] = false;
              }
            }
          }
         
        },
        error: (err) => {
          if (contest.id) { 
            this.contestAccess[contest.id] = false;
          }
          console.error(`Error checking access for contest ${contest.id}`, err);
        },
      });
      if (contest.id) { 
        this.contestAccess[contest.id] = false;
      }
    });
  }

  getDetailContest(id: number): void {
    const contest = this.contests.find((c) => c.id === id);
    if (contest) {
      this.selectedContest = contest;
      this.showRegistration = true;
    }
  }

  onCancelRegistration(): void {
    this.showRegistration = false;
    this.selectedContest = undefined;
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.getAllContests(this.currentPage, 5);
  }
  trackByContestId(index: number, contest: Contest): number | undefined {
    return contest.id;
  }
  
}
