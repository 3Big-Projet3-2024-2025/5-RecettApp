import { Component } from '@angular/core';
import { ContestService } from '../services/contest.service';
import { Contest } from '../models/contest';
import { KeycloakService } from 'keycloak-angular';
import { Router } from '@angular/router';
import { RegistrationComponent } from '../registration/registration.component';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-available-contest',
  standalone: true,
  imports: [CommonModule, RegistrationComponent],
  templateUrl: './available-contest.component.html',
  styleUrl: './available-contest.component.css'
})
export class AvailableContestComponent {
  contests: any[] = [];
  currentContest: Contest = this.initContest();
  selectedContest?: Contest;
  showRegistration = false;
  totalPages: number = 0;
  currentPage: number = 0;
  
  initContest(): Contest {
    return { title: "", max_participants: 0, start_date: "", end_date: "", status: "" };
  }

  constructor(
    private contestService: ContestService,
    private keycloakService : KeycloakService,
    private router : Router
  ) {}

  ngOnInit(): void {
    this.getAllContests(this.currentPage, 5);
    //console.log(this.keycloakService.getToken());
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.getAllContests(this.currentPage,5);
  }

  getAllContests(page: number = 0, size: number): void {
    const sub = this.contestService.getAllContests(page, size).subscribe({
      next: (data) => {
        console.log(data.content); 
        this.contests = data.content; 
        this.totalPages = data.totalPages;
        sub.unsubscribe();
      },
      error: (err) => {
        console.error("ERROR GETALLCONTESTS", err);
        sub.unsubscribe();
      }
    });
  }

  onCancelRegistration(): void {
    this.showRegistration = false;
    this.selectedContest = undefined;
  }

  getDetailContest(id: number): void{
    //this.router.navigate(['/available-contests', id]);

    const contest = this.contests.find(c => c.id === id);
    if(contest){
      this.selectedContest = contest;
      this.showRegistration = true;
    }
  }
  
}


