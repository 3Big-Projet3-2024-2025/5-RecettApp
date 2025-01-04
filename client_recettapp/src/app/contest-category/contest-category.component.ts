import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ContestCategory } from '../models/contest-category';
import { ContestCategoryService } from '../services/contest-category.service';
import { CommonModule } from '@angular/common';
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-contest-category',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './contest-category.component.html',
  styleUrls: ['./contest-category.component.css'],
})
export class ContestCategoryComponent implements OnInit {
  categories: ContestCategory[] = []; // All categories
  filteredCategories: ContestCategory[] = []; // Categories for the current page
  currentPage: number = 1; // Current page number
  pageSize: number = 5; // Number of categories per page
  totalCategories: number = 0; // Total number of categories
  categoryForm: FormGroup; // Form for adding or editing categories
  isEditing = false; // Flag to indicate editing mode
  currentId: number | null = null; // ID of the category being edited

  constructor(
    private fb: FormBuilder, // For creating forms
    private categoryService: ContestCategoryService, // To interact with the backend
    private keycloakService: KeycloakService
  ) {
    // Initialize the form with validation
    this.categoryForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(8)]],
      description: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(8)]],
    });
  }

  // Load all categories when the component initializes
  ngOnInit(): void {
    this.loadCategories();
  }

  // Fetch all categories from the backend
  async loadCategories(): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.categoryService.getAllCategories(token).subscribe(
      (data) => {
        this.categories = data;
        this.totalCategories = data.length;
        this.applyPagination(); // Update pagination
      },
      (error) => {
        console.error('Error loading categories:', error);
      }
    );
  }

  // Apply pagination logic
  applyPagination(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.filteredCategories = this.categories.slice(startIndex, endIndex);
  }

  // Handle page changes
  onPageChange(page: number): void {
    this.currentPage = page;
    this.applyPagination();
  }

  // Calculate total pages based on total categories
  get totalPages(): number {
    return Math.ceil(this.totalCategories / this.pageSize);
  }

  // Save or update a category
  async saveCategory(): Promise<void> {
    if (this.categoryForm.invalid) return;

    const category = this.categoryForm.value;

    if (this.isEditing && this.currentId !== null) {
      const token = await this.keycloakService.getToken();
      // Update existing category
      this.categoryService.updateCategory(this.currentId, category, token).subscribe(
        () => {
          this.loadCategories();
          this.resetForm();
        },
        (error) => console.error('Error updating category:', error)
      );
    } else {
      const token = await this.keycloakService.getToken();
      // Create new category
      this.categoryService.createCategory(category, token).subscribe(
        () => {
          this.loadCategories();
          this.resetForm();
        },
        (error) => console.error('Error creating category:', error)
      );
    }
  }

  // Edit an existing category
  editCategory(category: ContestCategory): void {
    this.categoryForm.patchValue(category);
    this.currentId = category.id!;
    this.isEditing = true;
  }

  // Delete a category
  async deleteCategory(id: number): Promise<void> {
    if (confirm('Are you sure you want to delete this category?')) {
      const token = await this.keycloakService.getToken();
      this.categoryService.deleteCategory(id, token).subscribe(
        () => {
          this.categories = this.categories.filter((category) => category.id !== id);
          this.totalCategories = this.categories.length;

          // Adjust the current page if the last page becomes empty
          if (this.currentPage > this.totalPages && this.currentPage > 1) {
            this.currentPage--;
          }

          this.applyPagination();
        },
        (error) => console.error('Error deleting category:', error)
      );
    }
  }

  // Reset the form and exit editing mode
  resetForm(): void {
    this.categoryForm.reset();
    this.isEditing = false;
    this.currentId = null;
  }
}
