import { Component, OnInit } from '@angular/core';
import { ContestCategoryService } from '../services/contest-category.service';
import { ContestCategory } from '../models/contest-category';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contest-category',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './contest-category.component.html',
  styleUrls: ['./contest-category.component.css'],
})
export class ContestCategoryComponent implements OnInit {
  categories: ContestCategory[] = [];
  currentCategory: ContestCategory = { title: '', description: '' };
  isEditing = false; // To differentiate between creation and editing
  showForm = false; // To show/hide the form

  constructor(private categoryService: ContestCategoryService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  // Load all categories
  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe((data) => {
      this.categories = data;
    });
  }

  // Open the form to add a new category
  addCategory(): void {
    this.currentCategory = { title: '', description: '' };
    this.isEditing = false;
    this.showForm = true;
  }

  // Load data for editing
  editCategory(category: ContestCategory): void {
    this.currentCategory = { ...category }; // Clone to avoid direct modification
    this.isEditing = true;
    this.showForm = true;
  }

  // Delete a category
  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe(() => {
        this.loadCategories();
      });
    }
  }

  // Save a category (create or update)
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

  // Cancel editing/creation
  cancel(): void {
    this.showForm = false;
    this.currentCategory = { title: '', description: '' };
  }
}
