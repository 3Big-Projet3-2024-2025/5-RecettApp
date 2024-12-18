import { Component, OnInit } from '@angular/core';
import { UsersService } from '../Services/users.service';
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
        console.error('Erreur lors du chargement des utilisateurs :', error);
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

 // Fonction de recherche dynamique
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


  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      this.usersService.delete(id).subscribe(() => {
        this.loadUsers(); // Recharger la liste après suppression
      });
    }
  }

}
