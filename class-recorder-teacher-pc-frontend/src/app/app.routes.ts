import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { Routes } from '@angular/router';
import { CourseListComponent } from './components/course-list/course-list.component';
import { MycourseComponent } from './components/mycourse/mycourse.component';
import { VideoFileComponent } from './components/video-file/video-file.component';

export const routes: Routes = [
    {
        path: 'loginregister', component: LoginRegisterComponent
    },
    {
        path: 'courselist', component: CourseListComponent
    },
    {
        path: 'mycourse/:id', component: MycourseComponent
    },
    {
        path: 'videofile/:name', component: VideoFileComponent
    },
    {
        path: '',
        redirectTo: '/loginregister',
        pathMatch: 'full'
    }
];
