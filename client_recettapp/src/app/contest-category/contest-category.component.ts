import { Component, OnInit } from '@angular/core';
import { ContestCategoryService } from '../services/contest-category.service';
import { ContestCategory } from '../models/contest-category';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contest-category',
  standalone:true,
  imports:[FormsModule,CommonModule],
  templateUrl: './contest-category.component.html',
  styleUrls: ['./contest-category.component.css'],
})
export class ContestCategoryComponent implements OnInit {
  categories: ContestCategory[] = [];
  currentCategory: ContestCategory = { title: '', description: ''};
  isEditing = false; // Pour différencier création et édition
  showForm = false; // Pour afficher/masquer le formulaire

  constructor(private categoryService: ContestCategoryService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  // Charger toutes les catégories
  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe((data) => {
      this.categories = data;
    });
  }

  // Ouvrir le formulaire pour ajouter une catégorie
  addCategory(): void {
    this.currentCategory = { title: '', description: '' };
    this.isEditing = false;
    this.showForm = true;
  }

  // Charger les données pour édition
  editCategory(category: ContestCategory): void {
    this.currentCategory = { ...category }; // Cloner pour éviter de modifier directement
    this.isEditing = true;
    this.showForm = true;
  }

  // Supprimer une catégorie
  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe(() => {
        this.loadCategories();
      });
    }
  }

  // Sauvegarder une catégorie (création ou mise à jour)
  saveCategory(): void {
    if (this.isEditing) {
      this.categoryService.updateCategory(this.currentCategory.id!, this.currentCategory).subscribe(() => {
        this.loadCategories();
        this.showForm = false;
      });
    } else {
      this.categoryService.createCategory(this.currentCategory).subscribe(() => {
        this.loadCategories();
        this.showForm = false;
      });
    }
  }

  // Annuler l'édition/création
  cancel(): void {
    this.showForm = false;
    this.currentCategory = { title: '', description: '' };
  }
}
