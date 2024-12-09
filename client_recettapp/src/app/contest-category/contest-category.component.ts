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
  errorMessage: string = ''; // To display validation or API errors

  constructor(private categoryService: ContestCategoryService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  // Load all categories
  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe(
      (data) => {
        this.categories = data;
      },
      (error) => {
        this.errorMessage = 'Failed to load categories.';
      }
    );
  }

  // Open the form to add a new category
  addCategory(): void {
    this.currentCategory = { title: '', description: '' };
    this.isEditing = false;
    this.showForm = true;
    this.errorMessage = '';
  }

  // Load data for editing
  editCategory(category: ContestCategory): void {
    this.currentCategory = { ...category };
    this.isEditing = true;
    this.showForm = true;
    this.errorMessage = '';
  }

  // Delete a category
  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe(
        () => {
          this.loadCategories();
        },
        (error) => {
          this.errorMessage = `Failed to delete category with ID ${id}.`;
        }
      );
    }
  }

  // Save a category (create or update)
  saveCategory(): void {
    if (!this.currentCategory.title.trim() || !this.currentCategory.description.trim()) {
      this.errorMessage = 'Both title and description are required.';
      return;
    }

    if (this.isEditing) {
      this.categoryService.updateCategory(this.currentCategory.id!, this.currentCategory).subscribe(
        () => {
          this.loadCategories();
          this.showForm = false;
        },
        (error) => {
          this.errorMessage = `Failed to update category with ID ${this.currentCategory.id}.`;
        }
      );
    } else {
      this.categoryService.createCategory(this.currentCategory).subscribe(
        () => {
          this.loadCategories();
          this.showForm = false;
        },
        (error) => {
          this.errorMessage = 'Failed to create the category.';
        }
      );
    }
  }

  // Cancel editing/creation
  cancel(): void {
    this.showForm = false;
    this.currentCategory = { title: '', description: '' };
    this.errorMessage = '';
  }
}
