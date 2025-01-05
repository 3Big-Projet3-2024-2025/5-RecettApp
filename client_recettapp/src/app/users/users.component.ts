import { Component, OnInit } from '@angular/core';
import { UsersService } from '../services/users.service';
import { CommonModule } from '@angular/common';
import { User } from '../models/users';
import { FormsModule } from '@angular/forms';
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  searchTerm: string = '';
  currentPage: number = 1;
  pageSize: number = 5;
  totalUsers: number = 0;
  searchEmail: string = '';
  searchedUser: User | null = null;
  errMessage: String = "";
  err: boolean = false;

  constructor(private usersService: UsersService, private keycloakService: KeycloakService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  async loadUsers(): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.usersService.findAll(token).subscribe(
      (data: User[]) => {
        console.log(data);
        this.users = data;
        this.totalUsers = data.length;
        this.paginateUsers();
      },
      (error: any) => {
        console.error('Error loading users :', error);
      }
    );
  }

  paginateUsers(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.filteredUsers = this.users.slice(startIndex, endIndex);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.paginateUsers();
  }

  get totalPages(): number {
    return Math.ceil(this.totalUsers / this.pageSize);
  }

 onSearch(): void {
  if (this.searchTerm.trim()) {
    this.filteredUsers = this.users.filter(user =>
      user.firstName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.lastName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.email.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  } else {
    this.filteredUsers = [...this.users];
  }
  this.paginateUsers();
}

async onSearchByEmail(): Promise<void> {
  if (this.searchEmail.trim()) {
    const token = await this.keycloakService.getToken();
    this.usersService.findByEmail(this.searchEmail, token).subscribe(
      (user: User) => {
        this.searchedUser = user;
      },
      (error: any) => {
        console.error('User not found or search error :', error);
        this.searchedUser = null;
      }
    );
  }
}


  async onDelete(id: number): Promise<void> {
    const token = await this.keycloakService.getToken();
    if (confirm('Are you sure you want to delete this user?')) {
      const sub = this.usersService.delete(id, token).subscribe(({
        next: () => {
          this.err = false;
          this.loadUsers();
        }, error: (err) => {
          this.err = true;
          this.errMessage = `Error during the process : ${err.error.error}`;
          console.log(this.errMessage);
        }

      }));
    }
  }

  async toggleBlockStatus(email: string, isBlocked: boolean): Promise<void> {
    const token = await this.keycloakService.getToken();
    if (isBlocked) {
      this.usersService.unblockUser(email, token).subscribe(({
        next: () => {
        }, error: (error) => {
          console.error('Error unblocking user:', error.error.error);
        }
      }));
    } else {
      this.usersService.blockUser(email, token).subscribe(({
        next: () => {
        }, error: (error) => {
          console.error('Error blocking user:', error.error.error);
        }
      }));
    }
    window.location.reload();
  }

}
