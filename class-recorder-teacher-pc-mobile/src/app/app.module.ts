import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';

import { ServerIpPage } from '../pages/server-ip/server-ip';
import { RecordPage } from '../pages/record/record';
import { TabsPage } from '../pages/tabs/tabs';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { ReactiveFormsModule } from '@angular/forms';
import { ServerConnectionService } from './services/server-connection.service';
import { BackgroundMode } from '@ionic-native/background-mode';
import { HttpModule } from '@angular/http';
import { SpinnerDialog } from '@ionic-native/spinner-dialog';
import { Insomnia } from '@ionic-native/insomnia';

import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer';
import { Media } from '@ionic-native/media';
import { File } from '@ionic-native/file';
import { LoginModal } from './modals/login/login-modal';
import { LoginService } from './services/login.service';
import { RecordStateComponent } from '../pages/record/record-state/record-state-component';
import { AddVideoModal } from './modals/add-video/add-video';
import { WebSocketRecord } from './services/websocket-services/WebSocketRecord';
import { WebsocketRecordService } from './services/websocket-services/WebSocketRecordService';
import { RecordStateService } from './services/record-state.service';
import { TokenService } from './services/token.service';
import { UploadAudioService } from './services/upload-audio.service';
import { RecordTimeComponent } from '../pages/record/record-time/record-time-component';
import { AndroidPermissions } from '@ionic-native/android-permissions';

@NgModule({
  declarations: [
    MyApp,
    ServerIpPage,
    RecordPage,
    TabsPage,
    RecordStateComponent,
    LoginModal,
    AddVideoModal,
    RecordTimeComponent
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    ReactiveFormsModule,
    HttpModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    ServerIpPage,
    RecordPage,
    TabsPage,
    LoginModal,
    RecordStateComponent,
    AddVideoModal,
    RecordTimeComponent
  ],
  providers: [
    StatusBar,
    SplashScreen,
    ServerConnectionService,
    LoginService,
    WebSocketRecord,
    WebsocketRecordService,
    RecordStateService,
    TokenService,
    FileTransfer,
    FileTransferObject,
    UploadAudioService,
    AndroidPermissions,
    BackgroundMode,
    Insomnia,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    Media,
    SpinnerDialog,
    File
  ]
})
export class AppModule {}
