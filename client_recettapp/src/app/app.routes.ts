import { Routes } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { RecipeTypeComponent } from './recipe-type/recipe-type.component';

export const routes: Routes = [
    
        { path: '', redirectTo: 'Contest', pathMatch: 'full' },
        { path: 'Contest', component: ContestCategoryComponent },  
        { path:'RecipeType',component:RecipeTypeComponent},
    
];
