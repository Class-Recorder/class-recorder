import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MatToolbarModule, MatIconModule, MatButtonModule,
    MatMenuModule, MatGridListModule, MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatTabsModule, MatListModule, MatSnackBarModule,
    MatProgressSpinnerModule, MatDialogModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpModule } from '@angular/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './components/app/app.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { routes } from './app.routes';
import { LoginComponent } from './components/login/login.component';
import { CourseListComponent } from './components/course-list/course-list.component';

import {LoginService} from './services/login.service';
import {TeacherService} from './services/teacher.service';
import {LocalVideoService} from './services/local-video.service';
import {CourseService} from './services/course.service';
import {GenericDataBindingService} from './services/bind-services/generic-data-binding.service';
import {GlobalInfoService} from './services/bind-services/global-info-service';
import {WebsocketService } from './services/websocket-services/WebSocketService';
import {WebSocketProcessInfo} from './services/websocket-services/WebSocketProcessInfo';
import { CourseCardComponent } from './components/course-card/course-card.component';
import { MycourseComponent } from './components/mycourse/mycourse.component';
import { MycourseLocalvideosComponent } from './components/mycourse-localvideos/mycourse-localvideos.component';
import { VideoFileComponent, VideoCutDialogComponent } from './components/video-file/video-file.component';
import { ConsoleComponentComponent } from './components/console-component/console-component.component';
import { CutVideoProgressComponent } from './components/cut-video-progress/cut-video-progress.component';

@NgModule({
    declarations: [
        AppComponent,
        LoginRegisterComponent,
        LoginComponent,
        CourseListComponent,
        CourseCardComponent,
        MycourseComponent,
        MycourseLocalvideosComponent,
        VideoFileComponent,
        ConsoleComponentComponent,
        VideoCutDialogComponent,
        CutVideoProgressComponent
    ],
    imports: [
        RouterModule.forRoot(routes),
        BrowserModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        MatToolbarModule,
        MatIconModule,
        MatButtonModule,
        MatMenuModule,
        MatGridListModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatTabsModule,
        MatListModule,
        MatSnackBarModule,
        MatProgressSpinnerModule,
        MatDialogModule,
        FlexLayoutModule
    ],
    providers: [LoginService, TeacherService, GenericDataBindingService, LocalVideoService,
        GlobalInfoService, CourseService, WebsocketService, WebSocketProcessInfo],
    bootstrap: [AppComponent],
    entryComponents: [VideoCutDialogComponent]
})
export class AppModule { }
