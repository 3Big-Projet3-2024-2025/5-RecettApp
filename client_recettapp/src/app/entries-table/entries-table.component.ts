import { Component } from '@angular/core';
import { Entry } from '../models/entry';
import { EntriesService } from '../services/entries.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-entries-table',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './entries-table.component.html',
  styleUrl: './entries-table.component.css'
})
export class EntriesTableComponent {
  entries: Entry[] = [];
  currentEntry: Entry = this.initEntry();
  isEditing = false;
  showForm = false;

  initEntry():Entry{
    return {};
  }

  constructor(private entriesService: EntriesService) {}

  ngOnInit():void {
    this.getAllEntries();
  }

  getAllEntries(): void {
    const sub = this.entriesService.getAllEntries().subscribe({
      next: (data) => {
        console.log(data);
        this.entries = data;
        sub.unsubscribe();
      }, error: (err) => {
        console.log("ERROR GETALLENTRIES");
        sub.unsubscribe();
      }
    })
  }

  deleteEntry(idEntry: number){
    if (confirm('Are you sure you want to delete this entry?')) {
      this.entriesService.deleteEntry(idEntry).subscribe({
        next: () => {
          this.getAllEntries();
        }, error: (err) => {
          console.error(err);
        }
      })
    }
  }
}
