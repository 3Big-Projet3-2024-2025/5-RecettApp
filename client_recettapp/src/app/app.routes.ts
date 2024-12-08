import { Routes } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { ContestTableComponent } from './contest-table/contest-table.component';

import { RecipeTypeComponent } from './recipe-type/recipe-type.component';

import { UsersComponent } from './users/users.component';


export const routes: Routes = [

        { path: '', redirectTo: 'Contest', pathMatch: 'full' },

        { path: 'ContestCategory', component: ContestCategoryComponent },  
        { path:'RecipeType',component:RecipeTypeComponent},
        { path: 'users', component : UsersComponent},
        { path: 'contests', component: ContestTableComponent},


];
