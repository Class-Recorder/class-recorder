import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { Routes } from '@angular/router';
import { CourseListComponent } from './components/course-list/course-list.component';
import { MycourseComponent } from './components/mycourse/mycourse.component';
import { VideoFileComponent } from './components/video-file/video-file.component';
import { CutVideoProgressComponent } from './components/cut-video-progress/cut-video-progress.component';
import { AddVideoComponent } from './components/add-video/add-video.component';
import { VideoControlComponent } from './components/video-control/video-control.component';
import { UploadVideoYoutubeComponent } from './components/upload-youtube/upload-video-youtube.component';
import { UploadVideoYoutubeProgressComponent } from './components/upload-youtube-progress/upload-video-youtube-progress.component';
import { VideoYoutubeComponent } from './components/video-youtube/video-youtube.component';
import { AddCourseComponent } from './components/add-course/add-course.component';
import { UpdateCourseComponent } from './components/update-course/update-course.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
    {
        path: 'login', component: LoginRegisterComponent
    },
    {
        path: 'register', component: RegisterComponent
    },
    {
        path: 'courselist/:teacherId', component: CourseListComponent
    },
    {
        path: 'courselist-add/:teacherId', component: AddCourseComponent
    },
    {
        path: 'courselist-update/:courseId', component: UpdateCourseComponent
    },
    {
        path: 'mycourse/:id', component: MycourseComponent
    },
    {
        path: 'videofile/:name', component: VideoFileComponent
    },
    {
        path: 'cut-video-progress', component: CutVideoProgressComponent
    },
    {
        path: 'record-video', component: AddVideoComponent
    },
    {
        path: 'uploadvideo/:courseid/:filename', component: UploadVideoYoutubeComponent
    },
    {
        path: 'uploadvideo-progress', component: UploadVideoYoutubeProgressComponent
    },
    {
        path: 'video-youtube/:id', component: VideoYoutubeComponent
    },
    {
        path: '',
        redirectTo: '/login',
        pathMatch: 'full'
    }
];
