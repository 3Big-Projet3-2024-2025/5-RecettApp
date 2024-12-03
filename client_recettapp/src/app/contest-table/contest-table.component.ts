import { Component } from '@angular/core';
import { Contest } from '../models/contest';
import { ContestService } from '../services/contest.service';

@Component({
  selector: 'app-contest-table',
  standalone: true,
  imports: [],
  templateUrl: './contest-table.component.html',
  styleUrl: './contest-table.component.css'
})
export class ContestTableComponent {
  contests: Contest[] = [];
  currentCategory: Contest[] = [];

  constructor(private contestService : ContestService){}

  ngOnInit(): void{
    this.getAllContests();
  }

  getAllContests(){
    const sub = this.contestService.getAllContests().subscribe({
      next: (data) => {
        console.log(data);
        sub.unsubscribe();
      }, error: (err) => {
        console.log("ERR GETALLCONTESTS");
        sub.unsubscribe();
      }
    })
  }
}
