import { Component, OnInit } from '@angular/core';
import { Contest } from '../models/contest';
import { ContestService } from '../services/contest.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-contest-table',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './contest-table.component.html',
  styleUrl: './contest-table.component.css'
})
export class ContestTableComponent {
  contests: Contest[] = [];
  currentContest: Contest = this.initContest();
  isEditing = false;
  showForm = false;

  constructor(private contestService: ContestService,private router: Router) { }

  ngOnInit(): void {
    this.getAllContests();
    //this.convertContestDates();
  }

  /*
  convertContestDates(): void {
    this.contests.forEach(contest => {
      if (contest.start_date) {
        contest.start_date = this.convertToLocalDateFormat(contest.start_date);
      }
      if (contest.end_date) {
        contest.end_date = this.convertToLocalDateFormat(contest.end_date);
      }
    });
  }
  */


  convertToLocalDateFormat(date: string): string {
    const localDate = new Date(date);
    // Format "yyyy-MM-ddTHH:mm"
    return localDate.toISOString().slice(0, 16);
  }

  getAllContests(): void {
    const sub = this.contestService.getAllContests().subscribe({
      next: (data) => {
        console.log(data);
        this.contests = data;
        sub.unsubscribe();
      }, error: (err) => {
        console.log("ERROR GETALLCONTESTS");
        sub.unsubscribe();
      }
    })
  }

  addContest(): void {
    this.currentContest = this.initContest();
    this.isEditing = false;
    this.showForm = true;

  }


  checkContest(contest: Contest): boolean {
    if (!contest.title || contest.max_participants <= 0 || !contest.status) {
      console.error('Contest mandatory fields are not filled in.');
      return false;
    }
    if (!contest.start_date || !contest.end_date || contest.start_date.trim() == "" || contest.end_date.trim() == "") {
      console.error('The dates are not provided.');
      return false;
    }


    const startDate = new Date(contest.start_date);
    const endDate = new Date(contest.end_date);

    if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
      console.error('The dates provided are not valid.')
      return false;
    }

    if (startDate > endDate) {
      console.error('The start date cannot be later than the end date.');
      return false;
    }


    return true;

  }

  convertDateToIsoFormat(inputDate: string | undefined): string {
    if (inputDate == undefined) {
      return "";
    }
    const date = new Date(inputDate);
    return date.toISOString();
  }

  editContest(contest: Contest): void {
    this.currentContest = { ...contest }; 
    this.isEditing = true;
    this.showForm = true;
  }

  deleteContest(idContest: number): void {
    if (confirm('Are you sure you want to delete this contest?')) {
      this.contestService.deleteContest(idContest).subscribe(() => {
        this.getAllContests();
      });
    }
  }

  saveContest(): void {
    if (this.isEditing) {
      if (this.checkContest(this.currentContest)) {
        this.currentContest.start_date = this.convertDateToIsoFormat(this.currentContest.start_date);
        this.currentContest.end_date = this.convertDateToIsoFormat(this.currentContest.end_date);
        const sub = this.contestService.updateContest(this.currentContest).subscribe({
          next: () => {
            this.getAllContests();
            this.showForm = false;
            this.currentContest = this.initContest();
          }, error: (err) => {
            console.log("ERROR UPDATE CONTEST");
            sub.unsubscribe();
          }
        })
      }
    } else {
      if (this.checkContest(this.currentContest)) {
        this.currentContest.start_date = this.convertDateToIsoFormat(this.currentContest.start_date);
        this.currentContest.end_date = this.convertDateToIsoFormat(this.currentContest.end_date);
        const sub = this.contestService.addContest(this.currentContest).subscribe({
          next: () => {
            this.getAllContests();
            this.showForm = false;
            this.currentContest = this.initContest();
          }, error: (err) => {
            console.log("ERROR POST CONTEST");
            sub.unsubscribe();
          }
        })
      }
    }
  }

  cancel(): void {
    this.showForm = false;
    this.currentContest = this.initContest();
  }

  initContest(): Contest {
    return { title: "", max_participants: 0, start_date: "", end_date: "", status: "" };
  }
  getAllRecipe(idContest: any){
    this.router.navigate(['recipe-contest/', idContest]);
  }
}
