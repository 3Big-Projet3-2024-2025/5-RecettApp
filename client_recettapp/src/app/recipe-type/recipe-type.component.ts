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
  recipeTypes: RecipeType[] = []; // List of recipe types
  recipeTypeForm: FormGroup; // Form group for the recipe type form
  isEditing = false; // Flag to check if we are editing or adding a new recipe type
  currentId: number | null = null; // Store the ID of the recipe type being edited

  constructor(
    private fb: FormBuilder,
    private recipeTypeService: RecipeTypeService
  ) {
    // Initialize the form group with validation rules
    this.recipeTypeForm = this.fb.group({
      name: [
        '', 
        [Validators.required, Validators.maxLength(255), Validators.minLength(3)],
      ],
    });
  }

  ngOnInit(): void {
    // Load all recipe types when the component is initialized
    this.loadRecipeTypes();
  }

  // Load all recipe types from the server
  loadRecipeTypes(): void {
    this.recipeTypeService.getAllRecipeTypes().subscribe(
      (data) => {
        this.recipeTypes = Array.isArray(data) ? data : []; // Check if data is an array
      },
      (error) => {
        console.error('Error loading recipe types:', error); // Log an error if loading fails
      }
    );
  }

  // Save a new recipe type or update an existing one
  saveRecipeType(): void {
    if (this.recipeTypeForm.invalid) return; // Do nothing if the form is invalid

    const recipeType = this.recipeTypeForm.value; // Get the form values

    // Check if we are editing or creating a new recipe type
    if (this.isEditing && this.currentId !== null) {
      // Update an existing recipe type
      this.recipeTypeService.updateRecipeType(this.currentId, recipeType).subscribe(
        () => {
          this.loadRecipeTypes(); // Reload the recipe types list
          this.resetForm(); // Reset the form
        },
        (error) => console.error('Error updating recipe type:', error) // Log error if update fails
      );
    } else {
      // Create a new recipe type
      this.recipeTypeService.createRecipeType(recipeType).subscribe(
        () => {
          this.loadRecipeTypes(); // Reload the recipe types list
          this.resetForm(); // Reset the form
        },
        (error) => console.error('Error creating recipe type:', error) // Log error if creation fails
      );
    }
  }

  // Populate the form with the recipe type to be edited
  editRecipeType(recipeType: RecipeType): void {
    this.recipeTypeForm.patchValue(recipeType); // Fill the form with the recipe type's data
    this.currentId = recipeType.id!; // Store the ID of the recipe type being edited
    this.isEditing = true; // Set editing mode to true
  }

  // Delete a recipe type by its ID
  deleteRecipeType(id: number): void {
    if (confirm('Are you sure you want to delete this recipe type?')) {
      this.recipeTypeService.deleteRecipeType(id).subscribe(
        () => {
          // Remove the deleted recipe type from the list
          this.recipeTypes = this.recipeTypes.filter((recipeType) => recipeType.id !== id);
        },
        (error) => console.error('Error deleting recipe type:', error) // Log error if deletion fails
      );
    }
  }

  // Reset the form and exit editing mode
  resetForm(): void {
    this.recipeTypeForm.reset(); // Clear the form fields
    this.isEditing = false; // Set editing mode to false
    this.currentId = null; // Clear the current ID
  }
}
