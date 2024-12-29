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
<<<<<<< HEAD
import { HomePageComponent } from './home-page/home-page.component';
import { EvaluationComponent } from './evaluation/evaluation.component';
=======
import { HomePageComponent } from './pages/home-page/home-page.component';
import { AvailableContestComponent } from './available-contest/available-contest.component';
>>>>>>> e3c98322e8f20019692ebbd20d7fea722835754b



export const routes: Routes = [

        { path: '', redirectTo: 'home', pathMatch: 'full' },
<<<<<<< HEAD
        { path: 'recipe', component: RecipeComponent },
        { path: 'recipe/:id', component: RecipeDetailComponent },
        { path: 'recipe/add/:idConstest', component: AddRecipeComponent },
        { path: 'ContestCategory', component: ContestCategoryComponent , canActivate: [KeycloakGuard]},
        { path:'RecipeType',component:RecipeTypeComponent},
        { path: 'users', component : UsersComponent},
        { path: 'contests', component: ContestTableComponent},
        { path: 'entries', component: EntriesTableComponent},
        { path: 'home', component: HomePageComponent},
        {path : 'evaluation' , component: EvaluationComponent}
=======
        { path: 'recipe', component: RecipeComponent , canActivate: [KeycloakGuard]},
        { path: 'recipe/:id', component: RecipeDetailComponent , canActivate: [KeycloakGuard]},
        { path: 'recipe/add/:idEntry', component: AddRecipeComponent , canActivate: [KeycloakGuard]},
        { path: 'ContestCategory', component: ContestCategoryComponent , canActivate: [KeycloakGuard]},
        { path:'RecipeType',component:RecipeTypeComponent , canActivate: [KeycloakGuard]},
        { path: 'users', component : UsersComponent , canActivate: [KeycloakGuard]},
        { path: 'contests', component: ContestTableComponent , canActivate: [KeycloakGuard]},
        { path: 'entries', component: EntriesTableComponent , canActivate: [KeycloakGuard]},
        { path: 'available-contests', component: AvailableContestComponent, canActivate: [KeycloakGuard]},
        { path: 'home', component: HomePageComponent}

>>>>>>> e3c98322e8f20019692ebbd20d7fea722835754b

];
