import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RecipeType } from '../models/recipe-type';
import { RecipeTypeService } from '../services/recipe-type.service';
import { CommonModule } from '@angular/common';
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-recipe-type',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './recipe-type.component.html',
  styleUrls: ['./recipe-type.component.css'],
})
export class RecipeTypeComponent implements OnInit {
  recipeTypes: RecipeType[] = []; // Complete list of recipe types
  filteredRecipeTypes: RecipeType[] = []; // Recipe types shown on the current page
  recipeTypeForm: FormGroup; // Form group for recipe type form
  currentPage: number = 1; // Current pagination page
  pageSize: number = 5; // Items per page
  totalRecipeTypes: number = 0; // Total number of recipe types
  isEditing = false; // Tracks if the form is in edit mode
  currentId: number | null = null; // ID of the recipe type being edited
  showForm: boolean = false; // Determines if the form is displayed

  constructor(

    private fb: FormBuilder, // Form builder for managing forms
    private recipeTypeService: RecipeTypeService, // Service to interact with the backend
    private keycloakService: KeycloakService

  ) {
    // Initialize the form with validation
    this.recipeTypeForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(3)]],
    });
  }

  // Load recipe types on component initialization
  ngOnInit(): void {
    this.loadRecipeTypes();
  }

  // Fetch recipe types from the backend
  async loadRecipeTypes(): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.recipeTypeService.getAllRecipeTypes(token).subscribe(
      (data) => {
        this.recipeTypes = Array.isArray(data) ? data : [];
        this.totalRecipeTypes = this.recipeTypes.length;
        this.applyPagination();
      },
      (error) => console.error('Error loading recipe types:', error)
    );
  }

  // Apply pagination logic
  applyPagination(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.filteredRecipeTypes = this.recipeTypes.slice(startIndex, endIndex);
  }

  // Handle pagination page change
  onPageChange(page: number): void {
    this.currentPage = page;
    this.applyPagination();
  }

  // Calculate total number of pages
  get totalPages(): number {
    return Math.ceil(this.totalRecipeTypes / this.pageSize);
  }

  // Save a new recipe type or update an existing one

  async saveRecipeType(): Promise<void> {
    if (this.recipeTypeForm.invalid) return; // Stop if the form is invalid


    const recipeType = this.recipeTypeForm.value;

    if (this.isEditing && this.currentId !== null) {
      const token = await this.keycloakService.getToken();
      // Update existing recipe type
      this.recipeTypeService.updateRecipeType(this.currentId, recipeType, token).subscribe(
        () => {
          this.loadRecipeTypes();
          this.resetForm();
        },
        (error) => console.error('Error updating recipe type:', error)
      );
    } else {

      const token = await this.keycloakService.getToken();
      // Create new recipe type
      this.recipeTypeService.createRecipeType(recipeType, token).subscribe(

        () => {
          this.loadRecipeTypes();
          this.resetForm();
        },
        (error) => console.error('Error creating recipe type:', error)
      );
    }
  }

  // Edit an existing recipe type
  editRecipeType(recipeType: RecipeType): void {
    this.recipeTypeForm.patchValue(recipeType);
    this.currentId = recipeType.id!;
    this.isEditing = true;
    this.showForm = true;
  }

  // Delete a recipe type
  async deleteRecipeType(id: number): Promise<void> {
    if (confirm('Are you sure you want to delete this recipe type?')) {
      const token = await this.keycloakService.getToken();
      this.recipeTypeService.deleteRecipeType(id, token).subscribe(
        () => {
          this.recipeTypes = this.recipeTypes.filter((recipeType) => recipeType.id !== id);
          this.totalRecipeTypes = this.recipeTypes.length;

          // Adjust current page if last page becomes empty
          if (this.currentPage > this.totalPages && this.currentPage > 1) {
            this.currentPage--;
          }

          this.applyPagination();
        },
        (error) => console.error('Error deleting recipe type:', error)
      );
    }
  }

  // Toggle the visibility of the form
  toggleForm(): void {
    this.showForm = !this.showForm;
  }

  // Reset the form and exit edit mode
  resetForm(): void {
    this.recipeTypeForm.reset();
    this.isEditing = false;
    this.currentId = null;
    this.showForm = false;
  }
}
