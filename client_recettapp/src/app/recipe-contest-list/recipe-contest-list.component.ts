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
import { EvaluationService } from '../services/evaluation.service';
import { RecipeRate } from '../models/recipe-rate';
import { RecipeComponentService } from '../services/recipe_Service/recipe-component.service';
import {KeycloakService} from "keycloak-angular";


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
MaxRecipeRate: RecipeRate[] = [];
searchTerm: string = '';
contestID: number |undefined;
entry: Entry = {};
contestIsFinish = false ;
recipeWin: Recipe = {
  id: 0,
  title: '',
  description: '',
  category: '',
  preparation_time: 0,
  cooking_time: 0,
  servings: 0,
  difficulty_level: '',
  instructions: '',
  masked: false,
  components: [],
  image: []
}

// Pagination properties
currentPage: number = 0;
itemsPerPage: number = 10;
totalPages: number = 0;


constructor(private service: RecipeService,private router:Router, private route: ActivatedRoute,private imaService: ImageServiceService,private entryService: EntriesService, private evaluationservice: EvaluationService
           , private recipeComponentService: RecipeComponentService, private keycloakService: KeycloakService
) {}



   ngOnInit(): void {
    const contestId = this.route.snapshot.paramMap.get('idContest');

    if (contestId != null) {
      this.checkContestAccess(+contestId);
    }

  }
  async checkContestAccess(contestId: number): Promise<void> {
    const token = await this.keycloakService.getToken();
    this.entryService.getEntryByUserMailAndIdContest(contestId, token).subscribe({
      next: (entry) => {
        if (!entry) {
          this.router.navigate(['/not-authorized']); // Redirection if entry is null
        } else {
          this.entry = entry;


          if (entry.status == 'waiting') {
            console.log("you have not completed your registration at the entry");
            this.router.navigate(['/not-authorized']);
          }
          this.getRecipe(contestId); 
          this.isContestFinish(entry);


        }
      },
      error: (error) => {
        console.log(error)

        this.router.navigate(['/not-authorized']); 


      },
    });
  }

  async getRecipe(contestId: number) {
    const token = await this.keycloakService.getToken();
    this.service.getRecipeByIdContest(contestId, token).subscribe(data => {
      const imageRequests = data.map(recipe => {
        if (recipe.photo_url) {
          return this.imaService.getImage(recipe.photo_url, token).pipe(
            map((blob: Blob) => {
              recipe.photo_url = URL.createObjectURL(blob);
              return recipe;
            })
          );
        }
        return of(recipe);
      });

      forkJoin(imageRequests).subscribe((updatedRecipes) => {
        this.recipes = this.shuffleArray(updatedRecipes) ;
        this.filteredRecipes = [...this.recipes];
        this.totalPages = Math.ceil(this.filteredRecipes.length / this.itemsPerPage);
        this.updateDisplayedRecipes();

        this.calculeRecipewithmaxrate();

      });
    });
  }

  isContestFinish(entry: Entry) {
    if (!entry || !entry.contest || !entry.contest.end_date) {
        this.contestIsFinish = false;
        console.error("Entry, contest, or end_date is missing.");
        return;
    }

    try {
        const currentDate = new Date();
        const contestEndDate = new Date(entry.contest.end_date);

        if (isNaN(contestEndDate.getTime())) {
            console.error("Invalid end_date format:", entry.contest.end_date);
            this.contestIsFinish = false;
            return;
        }

        this.contestIsFinish = currentDate > contestEndDate;
        console.log("Contest is finished:", this.contestIsFinish);
    } catch (error) {
        console.error("Error in isContestFinish:", error);
        this.contestIsFinish = false;
    }
}




calculeRecipewithmaxrate() {
  const ratePromises = this.recipes.map((recipe) => {
      return this.evaluationservice.getEvaluationsByRecipe(recipe.id).pipe(
          map((evaluations) => {
              const totalRate = evaluations.reduce((sum, evaluation) => sum + evaluation.rate, 0);
              return {
                  title: recipe.title,
                  totalRate: totalRate
              };
          })
      );
  });

  forkJoin(ratePromises).subscribe((rates) => {
      this.MaxRecipeRate = rates;
      console.log("Max Recipe Rate:", this.MaxRecipeRate);
      this.getRecipeWin();
  });
}






        this.getRecipeComponent();

      });
    });
  }

  async getRecipeComponent() {
    const token = await this.keycloakService.getToken();
    this.recipes.forEach(element => {
      this.recipeComponentService.getRecipeComponentsByIdRecipe(element.id, token).subscribe(component => {
        element.components = component;
      });
    })
  }

  searchRecipes(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredRecipes = this.recipes.filter(recipe =>
      recipe.title.toLowerCase().includes(term) ||
      recipe.description.toLowerCase().includes(term) ||

      recipe.category.toLowerCase().includes(term) ||
      recipe.components.some(component =>
        component.ingredient.alimentName.toLowerCase().includes(term)
      )

    );

    this.currentPage = 0;
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
    this.router.navigate(['recipe/detail', id, "backto"]);

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


  getRecipeWin() {
    if (!this.MaxRecipeRate || this.MaxRecipeRate.length === 0) {
        return null;
    }
    const maxRateRecipe = this.MaxRecipeRate.reduce((max, recipe) =>
        recipe.totalRate > max.totalRate ? recipe : max
    );
    this.findRecipeTitle(maxRateRecipe.title);
    return maxRateRecipe;
}



findRecipeTitle(title: string) {
  const recipe = this.recipes.find(r => r.title === title);
  if (!recipe) {
      console.error("Recipe not found:", title);
      return null;
  }
  this.recipeWin = recipe;
  console.log("Recipe with highest rate:", recipe);
  return recipe.title;

  
}

private shuffleArray(array: Recipe[]): Recipe[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }


WinnerContest(recipe : Recipe){

}


}



