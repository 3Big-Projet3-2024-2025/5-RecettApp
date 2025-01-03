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
import { HomePageComponent } from './pages/home-page/home-page.component';
import { AvailableContestComponent } from './available-contest/available-contest.component';
import { PaypalSuccessComponent } from './paypal-success/paypal-success.component';
import { PaypalCancelComponent } from './paypal-cancel/paypal-cancel.component';
import { RecipeContestListComponent } from './recipe-contest-list/recipe-contest-list.component';
import { UserInfoComponent } from './user-info/user-info.component';



export const routes: Routes = [

        { path: '', redirectTo: 'home', pathMatch: 'full' },
        { path: 'recipe', component: RecipeComponent , canActivate: [KeycloakGuard]},
        { path: 'recipe/:id', component: RecipeDetailComponent , canActivate: [KeycloakGuard]},
        { path: 'recipe-contest/:idContest', component: RecipeContestListComponent , canActivate: [KeycloakGuard]},
        { path: 'recipe/add/:idEntry', component: AddRecipeComponent , canActivate: [KeycloakGuard]},
        { path: 'ContestCategory', component: ContestCategoryComponent , canActivate: [KeycloakGuard]},
        { path:'RecipeType',component:RecipeTypeComponent , canActivate: [KeycloakGuard]},
        { path: 'users', component : UsersComponent , canActivate: [KeycloakGuard]},
        { path: 'contests', component: ContestTableComponent , canActivate: [KeycloakGuard]},
        { path: 'entries', component: EntriesTableComponent , canActivate: [KeycloakGuard]},
        { path: 'available-contests', component: AvailableContestComponent, canActivate: [KeycloakGuard]},
        { path: 'success', component:PaypalSuccessComponent},
        { path: 'cancel', component:PaypalCancelComponent},
        { path: 'home', component: HomePageComponent},
        {path:'User-info',component:UserInfoComponent}


];
