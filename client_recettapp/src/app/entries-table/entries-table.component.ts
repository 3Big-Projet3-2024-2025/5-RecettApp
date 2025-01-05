import { Component } from '@angular/core';
import { Entry } from '../models/entry';
import { EntriesService } from '../services/entries.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {KeycloakService} from "keycloak-angular";

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

  constructor(private entriesService: EntriesService, private keycloakService: KeycloakService) {}

  ngOnInit():void {
    this.getAllEntries();
  }

  async getAllEntries(): Promise<void> {
    const token = await this.keycloakService.getToken();
    const sub = this.entriesService.getAllEntries(token).subscribe({
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

  async deleteEntry(idEntry: number) {
    if (confirm('Are you sure you want to delete this entry?')) {
      const token = await this.keycloakService.getToken();
      this.entriesService.deleteEntry(idEntry, token).subscribe({
        next: () => {
          this.getAllEntries();
        }, error: (err) => {
          console.error(err);
        }
      })
    }
  }
}
