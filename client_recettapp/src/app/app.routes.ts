import { Routes } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { KeycloakGuard } from "./guard/keycloak.guard";

export const routes: Routes = [

        { path: '', redirectTo: 'Contest', pathMatch: 'full' },
        { path: 'Contest', component: ContestCategoryComponent, canActivate: [KeycloakGuard] },

];
