import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Recipe } from '../models/recipe';
import { Entry } from '../models/entry';
import { ActivatedRoute, Router } from '@angular/router';
import { map, of, forkJoin } from 'rxjs';
import { ImageServiceService } from '../services/image-service.service';
import { RecipeService } from '../services/recipe_Service/recipe.service';
import { EntriesService } from '../services/entries.service';

@Component({
  selector: 'app-recipe-contest-list',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './recipe-contest-list.component.html',
  styleUrl: './recipe-contest-list.component.css'
})
export class RecipeContestListComponent {

recipes: Recipe[] = [];
filteredRecipes: Recipe[] = [];
displayedRecipes: Recipe[] = [];
searchTerm: string = '';
contestID: number |undefined;
entry: Entry = {}

// Pagination properties
currentPage: number = 0;
itemsPerPage: number = 10;
totalPages: number = 0;

constructor(private service: RecipeService,private router:Router, private route: ActivatedRoute,private imaService: ImageServiceService,private entryService: EntriesService) {}


   ngOnInit(): void {
    const contestId = this.route.snapshot.paramMap.get('idContest');

    if (contestId != null) {
      this.checkContestAccess(+contestId);
    }

  }
  checkContestAccess(contestId: number): void {
    this.entryService.getEntryByUserMailAndIdContest(contestId).subscribe({
      next: (entry) => {
        if (!entry) {
          this.router.navigate(['/home']); // Redirection if entry is null
        } else {
          this.entry = entry;
          this.getRecipe(contestId); 
        }
      },
      error: (error) => {
        console.log(error)
        this.router.navigate(['/home']); 
      },
    });
  }
  getRecipe(contestId: number){
    this.service.getRecipeByIdContest(contestId).subscribe(data => {
      const imageRequests = data.map(recipe => {
        if (recipe.photo_url) {
          return this.imaService.getImage(recipe.photo_url).pipe(
            map((blob: Blob) => {
              recipe.photo_url = URL.createObjectURL(blob);
              return recipe;  
            })
          );
        }
        return of(recipe);  
      });
      
      forkJoin(imageRequests).subscribe((updatedRecipes) => {
        this.recipes = updatedRecipes;
        this.filteredRecipes = updatedRecipes;
        this.filteredRecipes = [...this.recipes];
        this.totalPages = Math.ceil(this.filteredRecipes.length / this.itemsPerPage);
        this.updateDisplayedRecipes();
      });
    });
  }
  searchRecipes(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredRecipes = this.recipes.filter(recipe =>
      recipe.title.toLowerCase().includes(term) || 
      recipe.description.toLowerCase().includes(term) || 
      recipe.category.toLowerCase().includes(term)
    );
    this.currentPage = 0; // Reset to the first page after filtering
    this.totalPages = Math.ceil(this.filteredRecipes.length / this.itemsPerPage);
    this.updateDisplayedRecipes();
  }

  sortRecipesByDifficulty(difficulty: string): void {
    this.filteredRecipes = this.recipes.filter(recipe => recipe.difficulty_level === difficulty);
  }

  resetFilters(): void {
    this.filteredRecipes = [...this.recipes];
    this.searchTerm = ''; // Clear the search term
    this.currentPage = 0; // Reset to the first page
    this.totalPages = Math.ceil(this.filteredRecipes.length / this.itemsPerPage);
    this.updateDisplayedRecipes();
  }
  // Sort recipes alphabetically by title
  sortRecipesByTitle(): void {
    this.filteredRecipes = [...this.filteredRecipes].sort((a, b) => a.title.localeCompare(b.title));
  }
  detailRecipe(id: number) {
    this.router.navigate(['/recipe', id]);
  }
  addRecipe() {
   this.router.navigate(['/recipe/add/', this.entry.id]);
  }
  
  updateDisplayedRecipes(): void {
    const startIndex = this.currentPage * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.displayedRecipes = this.filteredRecipes.slice(startIndex, endIndex);
  }


  goToPage(page: number): void {
    this.currentPage = page;
    this.updateDisplayedRecipes();
  }
  goToPrevious(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.updateDisplayedRecipes();
    }
  }
  goToNext(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.updateDisplayedRecipes();
    }
  }
}
