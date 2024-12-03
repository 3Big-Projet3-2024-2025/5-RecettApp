import { Routes } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { UsersComponent } from './users/users.component';

export const routes: Routes = [

        { path: '', redirectTo: 'Contest', pathMatch: 'full' },
        { path: 'Contest', component: ContestCategoryComponent },
        { path: 'users', component : UsersComponent},

];
