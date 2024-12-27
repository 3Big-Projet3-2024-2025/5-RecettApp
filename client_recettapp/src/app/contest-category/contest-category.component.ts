import { Component, OnInit } from '@angular/core';

import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { ContestCategory } from '../models/contest-category';
import { ContestCategoryService } from '../services/contest-category.service';
import { CommonModule } from '@angular/common';
import { NavBarComponent } from "../nav-bar/nav-bar.component";

@Component({
  selector: 'app-contest-category',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contest-category.component.html',
  styleUrls: ['./contest-category.component.css'],
})
export class ContestCategoryComponent implements OnInit {
  // List of categories
  categories: ContestCategory[] = [];

  // Form to create or edit a category
  categoryForm: FormGroup;

  // Check if we are editing a category
  isEditing = false;

  // ID of the category being edited
  currentId: number | null = null;

  constructor(
    private fb: FormBuilder, // Helps to build forms
    private categoryService: ContestCategoryService // Talks to the backend
  ) {
    // Create the form with validation rules
    this.categoryForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(8)]], // Title is required and must be between 8 and 255 characters
      description: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(8)]], // Description is required and must be between 8 and 255 characters
    });
  }

  // This runs when the page opens
  ngOnInit(): void {
    this.loadCategories(); // Load all categories
  }

  // Load all categories from the server
  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe(
      (data) => {
        // Check if the data is valid
        this.categories = Array.isArray(data) ? data : [];
      },
      (error) => {
        console.error('Error loading categories:', error); // Show error in the console if loading fails
      }
    );
  }

  // Save a new category or update an existing one
  saveCategory(): void {
    if (this.categoryForm.invalid) return; // Stop if the form is not valid

    const category = this.categoryForm.value; // Get data from the form

    if (this.isEditing && this.currentId !== null) {
      // If editing, update the category
      this.categoryService.updateCategory(this.currentId, category).subscribe(
        () => {
          this.loadCategories(); // Refresh the table
          this.resetForm(); // Clear the form
        },
        (error) => console.error('Error updating category:', error) // Show error in the console
      );
    } else {
      // If not editing, create a new category
      this.categoryService.createCategory(category).subscribe(
        () => {
          this.loadCategories(); // Refresh the table
          this.resetForm(); // Clear the form
        },
        (error) => console.error('Error creating category:', error) // Show error in the console
      );
    }
  }

  // When clicking the "Edit" button
  editCategory(category: ContestCategory): void {
    this.categoryForm.patchValue(category); // Fill the form with the category data
    this.currentId = category.id!; // Save the ID of the category
    this.isEditing = true; // Set editing mode to true
  }

  // When clicking the "Delete" button
  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      // Ask for confirmation
      this.categoryService.deleteCategory(id).subscribe(
        () => {
          // Remove the deleted category from the list
          this.categories = this.categories.filter((category) => category.id !== id);
        },
        (error) => console.error('Error deleting category:', error) // Show error in the console
      );
    }
  }

  // Clear the form and reset editing mode
  resetForm(): void {
    this.categoryForm.reset(); // Clear the form
    this.isEditing = false; // Turn off editing mode
    this.currentId = null; // Clear the ID
  }
}
