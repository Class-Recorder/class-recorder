import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'login-register', component: LoginRegisterComponent
    },
    {
        path: '',
        redirectTo: '/login-register',
        pathMatch: 'full'
    }
];
