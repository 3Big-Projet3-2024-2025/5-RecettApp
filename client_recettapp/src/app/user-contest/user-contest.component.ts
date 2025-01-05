import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { Contest } from '../models/contest';
import { ContestService } from '../services/contest.service';
import { EntriesService } from '../services/entries.service';
import { CommonModule } from '@angular/common';
import { RegistrationComponent } from '../registration/registration.component';
import { Entry } from '../models/entry';

@Component({
  selector: 'app-user-contest',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-contest.component.html',
  styleUrl: './user-contest.component.css'
})
export class UserContestComponent {
    contests: Contest[] = [];
    contestAccess: { [id: number]: boolean } = {};
    selectedContest?: Contest;
    showRegistration = false;
    totalPages: number = 0;
    currentPage: number = 0;
    contestIsFinish = false;
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
  
    
    trackByContestId(index: number, contest: Contest): number | undefined {
      return contest.id;
    }

    getAllRecipe(idContest: any){
      this.router.navigate(['recipe-contest/', idContest]);
    }
    
    isContestFinish(contest: Contest): boolean {
      if (!contest.end_date) {
        console.error("Contest end_date is missing.");
        return false;
      }
    
      try {
        const currentDate = new Date();
        const contestEndDate = new Date(contest.end_date);
    
        if (isNaN(contestEndDate.getTime())) {
          console.error("Invalid end_date format:", contest.end_date);
          return false;
        }
    
        return currentDate > contestEndDate;
      } catch (error) {
        console.error("Error in isContestFinish:", error);
        return false;
      }
    }
    
}
