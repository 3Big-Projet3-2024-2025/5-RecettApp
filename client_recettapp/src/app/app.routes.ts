import { Routes } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { RecipeComponent } from './recipe-list/recipe.component';
import { RecipeDetailComponent } from './recipe-detail/recipe-detail.component';
import { AddRecipeComponent } from './add-recipe/add-recipe.component';
import { KeycloakGuard } from "./guard/keycloak.guard";
import { ContestTableComponent } from './contest-table/contest-table.component';
import { RecipeTypeComponent } from './recipe-type/recipe-type.component';
import { UsersComponent } from './users/users.component';

import { EntriesTableComponent } from './entries-table/entries-table.component';
import { HomePageComponent } from './home-page/home-page.component';
import { EvaluationComponent } from './evaluation/evaluation.component';



export const routes: Routes = [

<<<<<<< HEAD
        { path: '', redirectTo: 'Contest', pathMatch: 'full' },
        { path: 'ContestCategory', component: ContestCategoryComponent , canActivate: [KeycloakGuard]},
        { path:'RecipeType',component:RecipeTypeComponent},
        { path: 'users', component : UsersComponent},
        { path: 'contests', component: ContestTableComponent},
        {path : 'evaluation' , component: EvaluationComponent}
=======
        { path: '', redirectTo: 'home', pathMatch: 'full' },
        { path: 'recipe', component: RecipeComponent },
        { path: 'recipe/:id', component: RecipeDetailComponent },
        { path: 'recipe/add/:idConstest', component: AddRecipeComponent },
        { path: 'ContestCategory', component: ContestCategoryComponent , canActivate: [KeycloakGuard]},
        { path:'RecipeType',component:RecipeTypeComponent},
        { path: 'users', component : UsersComponent},
        { path: 'contests', component: ContestTableComponent},
        { path: 'entries', component: EntriesTableComponent},
        { path: 'home', component: HomePageComponent}

>>>>>>> develop

];
