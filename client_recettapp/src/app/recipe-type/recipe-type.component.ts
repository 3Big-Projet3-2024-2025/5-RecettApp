import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RecipeType } from '../models/recipe-type';
import { RecipeTypeService } from '../services/recipe-type.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recipe-type',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './recipe-type.component.html',
  styleUrls: ['./recipe-type.component.css'],
})
export class RecipeTypeComponent implements OnInit {
  recipeTypes: RecipeType[] = []; // Full list of recipe types
  filteredRecipeTypes: RecipeType[] = []; // Recipe types displayed on the current page
  recipeTypeForm: FormGroup; // Form for adding or editing a recipe type
  currentPage: number = 1; // Current page number
  pageSize: number = 5; // Number of items per page
  totalRecipeTypes: number = 0; // Total number of recipe types
  isEditing = false; // Flag for edit mode
  currentId: number | null = null; // ID of the recipe type being edited

  constructor(
    private fb: FormBuilder, // Form builder for managing forms
    private recipeTypeService: RecipeTypeService // Service to interact with the backend
  ) {
    // Initialize the form with validation rules
    this.recipeTypeForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(3)]],
    });
  }

  // Load all recipe types when the component is initialized
  ngOnInit(): void {
    this.loadRecipeTypes();
  }

  // Fetch recipe types from the backend
  loadRecipeTypes(): void {
    this.recipeTypeService.getAllRecipeTypes().subscribe(
      (data) => {
        this.recipeTypes = Array.isArray(data) ? data : [];
        this.totalRecipeTypes = this.recipeTypes.length; // Update total count
        this.applyPagination(); // Apply pagination logic
      },
      (error) => {
        console.error('Error loading recipe types:', error);
      }
    );
  }

  // Apply pagination to the recipe types
  applyPagination(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize; // Start index for slicing
    const endIndex = startIndex + this.pageSize; // End index for slicing
    this.filteredRecipeTypes = this.recipeTypes.slice(startIndex, endIndex); // Slice the list
  }

  // Handle page changes
  onPageChange(page: number): void {
    this.currentPage = page;
    this.applyPagination();
  }

  // Calculate the total number of pages
  get totalPages(): number {
    return Math.ceil(this.totalRecipeTypes / this.pageSize);
  }

  // Save a new recipe type or update an existing one
  saveRecipeType(): void {
    if (this.recipeTypeForm.invalid) return; // Stop if the form is invalid

    const recipeType = this.recipeTypeForm.value;

    if (this.isEditing && this.currentId !== null) {
      // Update existing recipe type
      this.recipeTypeService.updateRecipeType(this.currentId, recipeType).subscribe(
        () => {
          this.loadRecipeTypes();
          this.resetForm();
        },
        (error) => console.error('Error updating recipe type:', error)
      );
    } else {
      // Create new recipe type
      this.recipeTypeService.createRecipeType(recipeType).subscribe(
        () => {
          this.loadRecipeTypes();
          this.resetForm();
        },
        (error) => console.error('Error creating recipe type:', error)
      );
    }
  }

  // Populate the form with the data of the recipe type being edited
  editRecipeType(recipeType: RecipeType): void {
    this.recipeTypeForm.patchValue(recipeType);
    this.currentId = recipeType.id!;
    this.isEditing = true;
  }

  // Delete a recipe type
  deleteRecipeType(id: number): void {
    if (confirm('Are you sure you want to delete this recipe type?')) {
      this.recipeTypeService.deleteRecipeType(id).subscribe(
        () => {
          this.recipeTypes = this.recipeTypes.filter((recipeType) => recipeType.id !== id); // Remove the deleted item
          this.totalRecipeTypes = this.recipeTypes.length; // Update the total count

          // Adjust the current page if the last page becomes empty
          if (this.currentPage > this.totalPages && this.currentPage > 1) {
            this.currentPage--;
          }

          this.applyPagination();
        },
        (error) => console.error('Error deleting recipe type:', error)
      );
    }
  }

  // Reset the form and exit edit mode
  resetForm(): void {
    this.recipeTypeForm.reset();
    this.isEditing = false;
    this.currentId = null;
  }
}
