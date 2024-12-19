import { Component } from '@angular/core';
import { ContestService } from '../services/contest.service';
import { Contest } from '../models/contest';

@Component({
  selector: 'app-available-contest',
  standalone: true,
  imports: [],
  templateUrl: './available-contest.component.html',
  styleUrl: './available-contest.component.css'
})
export class AvailableContestComponent {
  contests: Contest[] = [];
  currentContest: Contest = this.initContest();
  
  initContest(): Contest {
    return { title: "", max_participants: 0, start_date: "", end_date: "", status: "" };
  }

  constructor(private contestService: ContestService) {}

  ngOnInit(): void {
    this.getAllContests();
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


  
}
