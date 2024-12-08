import { Routes } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { RecipeComponent } from './recipe-list/recipe.component';
import { RecipeDetailComponent } from './recipe-detail/recipe-detail.component';

export const routes: Routes = [
    
        { path: '', redirectTo: 'Contest', pathMatch: 'full' },
        { path: 'Contest', component: ContestCategoryComponent },
        { path: 'recipe', component: RecipeComponent },
        { path: 'recipe/:id', component: RecipeDetailComponent },
        { path: 'recipe/add/:idConstest', component: RecipeDetailComponent }
        
    
];
