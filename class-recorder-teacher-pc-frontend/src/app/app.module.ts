import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MatToolbarModule, MatIconModule, MatButtonModule,
    MatMenuModule, MatGridListModule, MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatTabsModule, MatListModule, MatSnackBarModule,
    MatProgressSpinnerModule, MatDialogModule, MatCheckboxModule,
     MatProgressBar, MatProgressBarModule, MatDividerModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpModule, Http } from '@angular/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlyMaterialModule } from '@ngx-formly/material';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';


import { AppComponent, DialogGetIpComponent } from './components/app/app.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { routes } from './app.routes';
import { LoginComponent } from './components/login/login.component';
import { CourseListComponent } from './components/course-list/course-list.component';

import { LoginService } from './services/login.service';
import { TeacherService } from './services/teacher.service';
import { LocalVideoService } from './services/local-video.service';
import { CourseService } from './services/course.service';
import { RecordStateService } from './services/record-state.service';
import { GenericDataBindingService } from './services/bind-services/generic-data-binding.service';
import { GlobalInfoService } from './services/bind-services/global-info-service';
import { WebsocketProcessInfoService  } from './services/websocket-services/WebSocketProcessInfoService';
import { WebsocketRecordService  } from './services/websocket-services/WebSocketRecordService';
import { WebSocketProcessInfo } from './services/websocket-services/WebSocketProcessInfo';
import { WebSocketRecord } from './services/websocket-services/WebSocketRecord';
import { CourseCardComponent, DialogDeleteCourseComponent } from './components/course-card/course-card.component';
import { MycourseComponent } from './components/mycourse/mycourse.component';
import { MycourseLocalvideosComponent } from './components/mycourse-localvideos/mycourse-localvideos.component';
import { VideoFileComponent, VideoCutDialogComponent } from './components/video-file/video-file.component';
import { ConsoleComponentComponent } from './components/console-component/console-component.component';
import { CutVideoProgressComponent } from './components/cut-video-progress/cut-video-progress.component';
import { AddVideoComponent } from './components/add-video/add-video.component';
import { VideoControlComponent } from './components/video-control/video-control.component';
import { APP_BASE_HREF } from '@angular/common';
import { AppComponentTest } from './app.component';
import { UploadVideoYoutubeComponent } from './components/upload-youtube/upload-video-youtube.component';
import { YoutubeService } from './services/youtube.service';
import { WebSocketYoutubeProgressService } from './services/websocket-services/WebSocketYoutubeProgressService';
import { WebSocketYoutubeProgress } from './services/websocket-services/WebSocketYoutubeProgress';
import { UploadVideoYoutubeProgressComponent } from './components/upload-youtube-progress/upload-video-youtube-progress.component';
import { MycourseUploadedVideosComponent,
    DialogUpdateYoutubeVideoComponent,
    DialogDeleteVideoComponent} from './components/mycourse-uploadedvideos/mycourse-uploadedvideos.component';
import { VideoYoutubeComponent } from './components/video-youtube/video-youtube.component';
import { ArrayTypeComponent } from './ngx-formly/array.type';
import { AddCourseComponent } from './components/add-course/add-course.component';
import { UpdateCourseComponent } from './components/update-course/update-course.component';
import { RegisterComponent } from './components/register/register.component';
import { VideoChronoComponent } from './components/video-chrono/video-chrono.compontent';
import { ThemeService } from './services/theme.service';

@NgModule({
    declarations: [
        AppComponent,
        AppComponentTest,
        LoginRegisterComponent,
        LoginComponent,
        CourseListComponent,
        CourseCardComponent,
        MycourseComponent,
        MycourseLocalvideosComponent,
        VideoFileComponent,
        ConsoleComponentComponent,
        VideoCutDialogComponent,
        CutVideoProgressComponent,
        AddVideoComponent,
        VideoControlComponent,
        UploadVideoYoutubeComponent,
        UploadVideoYoutubeProgressComponent,
        MycourseUploadedVideosComponent,
        DialogUpdateYoutubeVideoComponent,
        DialogDeleteVideoComponent,
        DialogDeleteCourseComponent,
        DialogGetIpComponent,
        VideoYoutubeComponent,
        ArrayTypeComponent,
        UpdateCourseComponent,
        RegisterComponent,
        AddCourseComponent,
        VideoChronoComponent
    ],
    imports: [
        RouterModule.forRoot(routes),
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
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
        MatCheckboxModule,
        MatTabsModule,
        MatListModule,
        MatSnackBarModule,
        MatProgressSpinnerModule,
        MatDialogModule,
        MatProgressBarModule,
        MatDividerModule,
        FlexLayoutModule,
        ReactiveFormsModule,
        FormlyModule.forRoot({
              types: [
                { name: 'string', extends: 'input' },
                {
                  name: 'number',
                  extends: 'input',
                  defaultOptions: {
                    templateOptions: {
                      type: 'number',
                    },
                  },
                },
                {
                  name: 'integer',
                  extends: 'input',
                  defaultOptions: {
                    templateOptions: {
                      type: 'number',
                    },
                  },
                },
                { name: 'object', extends: 'formly-group' },
                { name: 'boolean', extends: 'checkbox' },
                { name: 'array', component: ArrayTypeComponent },
                { name: 'enum', extends: 'select' },
              ],
        }),
        FormlyMaterialModule,
        InfiniteScrollModule
    ],
    providers: [LoginService, TeacherService, GenericDataBindingService, LocalVideoService,
        GlobalInfoService, CourseService, RecordStateService, WebsocketProcessInfoService,
        WebsocketRecordService, WebSocketProcessInfo, WebSocketYoutubeProgressService,
        WebSocketYoutubeProgress,  WebSocketRecord, YoutubeService, ThemeService],
    bootstrap: [AppComponent],
    entryComponents: [VideoCutDialogComponent,
        DialogUpdateYoutubeVideoComponent, DialogDeleteVideoComponent,
        DialogDeleteCourseComponent, DialogGetIpComponent]
})
export class AppModule { }
