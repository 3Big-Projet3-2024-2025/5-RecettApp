import { Routes } from '@angular/router';
import { ContestCategoryComponent } from './contest-category/contest-category.component';
import { ContestTableComponent } from './contest-table/contest-table.component';

export const routes: Routes = [
    
        { path: '', redirectTo: 'Contest', pathMatch: 'full' },
        { path: 'Contest', component: ContestCategoryComponent },
        { path: 'contests', component: ContestTableComponent}
    
];
