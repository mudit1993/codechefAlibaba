import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard';

const appRoutes: Routes = [
    { path: '', component: DashboardComponent },
    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);