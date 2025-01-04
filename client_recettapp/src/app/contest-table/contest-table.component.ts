import { Component, OnInit } from '@angular/core';
import { Contest } from '../models/contest';
import { ContestService } from '../services/contest.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ContestCategory } from '../models/contest-category';
import { ContestCategoryService } from '../services/contest-category.service';

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
  categories!: ContestCategory[];
  totalPages: number = 0;
  currentPage: number = 0;

  constructor(
    private contestService: ContestService,
    private router: Router,
    private categoryService : ContestCategoryService
  ) { }

  ngOnInit(): void {
    this.getAllContests(this.currentPage, 5);
    //this.convertContestDates();
    this.getAllCategory();
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.getAllContests(this.currentPage,5);
  }


  convertToLocalDateFormat(date: string): string {
    const localDate = new Date(date);
    // Format "yyyy-MM-ddTHH:mm"
    return localDate.toISOString().slice(0, 16);
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

  formatDateForInput(date: string | undefined): string {
    if (!date) return "null";
    const parsedDate = new Date(date);
    const year = parsedDate.getFullYear();
    const month = String(parsedDate.getMonth() + 1).padStart(2, '0');
    const day = String(parsedDate.getDate()).padStart(2, '0');
    const hours = String(parsedDate.getHours()).padStart(2, '0');
    const minutes = String(parsedDate.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }

  editContest(contest: Contest): void {
    this.currentContest = { ...contest }; 
    this.currentContest.start_date = this.formatDateForInput(this.currentContest.start_date);
    this.currentContest.end_date = this.formatDateForInput(this.currentContest.end_date);

    this.isEditing = true;
    this.showForm = true;
  }

  deleteContest(idContest: number): void {
    if (confirm('Are you sure you want to delete this contest?')) {
      this.contestService.deleteContest(idContest).subscribe(() => {
        this.getAllContests(this.currentPage, 5);
        setTimeout(() => {
          if (this.contests.length === 0 && this.currentPage > 0) {
            this.currentPage--; 
            this.getAllContests(this.currentPage, 5);
          }
        }, 100);
      });
    }
  }

  convertDateToBackendFormat(inputDate: string | undefined): string {
    if (!inputDate) return "null";
    const date = new Date(inputDate);
    return date.toISOString(); 
  }

  saveContest(): void {
    console.log('Contest to save:', this.currentContest);
    if (this.isEditing) {
      if (this.checkContest(this.currentContest)) {
        this.currentContest.start_date = this.convertDateToBackendFormat(this.currentContest.start_date);
        this.currentContest.end_date = this.convertDateToBackendFormat(this.currentContest.end_date);
        if(this.currentContest.start_date === "null" || this.currentContest.end_date === "null"){
          throw new Error("Error Format Date");
        }
        const sub = this.contestService.updateContest(this.currentContest).subscribe({
          next: () => {
            this.getAllContests(this.currentPage, 5);
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
        this.currentContest.start_date = this.convertDateToBackendFormat(this.currentContest.start_date);
        this.currentContest.end_date = this.convertDateToBackendFormat(this.currentContest.end_date);
        if(this.currentContest.start_date === "null" || this.currentContest.end_date === "null"){
          throw new Error("Error Format Date");
        }
        const sub = this.contestService.addContest(this.currentContest).subscribe({
          next: () => {
            this.getAllContests(this.currentPage, 5);
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

  getAllCategory(){
    const sub = this.categoryService.getAllCategories().subscribe({
      next: (res) => {
        console.log("categories");
        this.categories = res;
        console.log(this.categories);
      }, error: (err) => {
        console.log('error while getting categories');
      }
    })
  }
}
