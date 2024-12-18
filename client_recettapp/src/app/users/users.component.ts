import { Component, OnInit } from '@angular/core';
import { UsersService } from '../services/users.service';
import { CommonModule } from '@angular/common';
import { User } from '../models/users';
import { FormsModule } from '@angular/forms';

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

  constructor(private usersService: UsersService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.usersService.findAll().subscribe(
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
      user.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.phone_number.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  } else {
    this.filteredUsers = [...this.users];
  }
  this.paginateUsers();
}

onSearchByEmail(): void {
  if (this.searchEmail.trim()) {
    this.usersService.findByEmail(this.searchEmail).subscribe(
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


  onDelete(id: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.usersService.delete(id).subscribe(() => {
        this.loadUsers();
      });
    }
  }

}
