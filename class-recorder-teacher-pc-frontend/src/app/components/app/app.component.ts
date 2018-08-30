import { Component, ElementRef, ViewChild, Inject } from '@angular/core';
import { TeacherService } from '../../services/teacher.service';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';
import { Teacher } from '../../classes/user/Teacher';
import { GlobalInfoService } from '../../services/bind-services/global-info-service';
import { OverlayContainer } from '@angular/cdk/overlay';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';
import { WebSocketYoutubeProgress } from '../../services/websocket-services/WebSocketYoutubeProgress';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';
import { YoutubeService } from '../../services/youtube.service';
import { LoginService } from '../../services/login.service';
import { Router, ActivatedRoute } from '@angular/router';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { RecordStateService } from '../../services/record-state.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {


    teacherId: number;
    currentTheme: string;
    youtubeUploading: boolean;
    isLogged: boolean;

    constructor(
        private _globalInfoService: GlobalInfoService,
        private _teacherService: TeacherService,
        private _overlayContainer: OverlayContainer,
        private _genericDataService: GenericDataBindingService,
        private _youtubeProgressService: WebSocketYoutubeProgress,
        private _youtubeService: YoutubeService,
        private _router: Router,
        private _loginService: LoginService,
        private dialog: MatDialog
    ) {}

    ngOnInit() {
        this.currentTheme = 'theme-dark';
        this.themeDark();
        this._loginService.reqIsLogged().then(() => {
            if (this._loginService.isLogged) {
                this.isLogged = this._loginService.isLogged;
                this.initNav(this._loginService.user.id);
                this.getYoutubeUploadState();
                this.listenYoutubeUpload();
            }
        });
        this._genericDataService.changeEmittedSubject('login-succesful').subscribe((teacherId) => {
            this.isLogged = this._loginService.isLogged;
            this.initNav(teacherId);
            this.getYoutubeUploadState();
            this.listenYoutubeUpload();
        });
    }

    initNav(id) {
        this._teacherService.getTeacherInfo(id).subscribe((teacherInfo) => {
            this.teacherId = teacherInfo.id;
            this._globalInfoService.teacherId = this.teacherId;
            this._router.navigate(['courselist', this.teacherId]);
            this._genericDataService.emitChangeSubject('get-recording-state');
        });
    }

    homeButtonAction() {
        const actualRoute = this._router.url;
        if (actualRoute === '/register' || actualRoute === '/login') {
            this._router.navigate(['']);
        } else {
            this._router.navigate(['courselist', this.teacherId]);
        }
    }

    themeDark() {
        this._overlayContainer.getContainerElement().classList.add('theme-dark');
        document.querySelector('html').style.background = '#818181';
        this.currentTheme = 'theme-dark';
    }

    themeIndigoPink() {
        this._overlayContainer.getContainerElement().classList.add('indigo-pink');
        document.querySelector('html').style.background = '#c2d6d6';
        this.currentTheme = 'indigo-pink';
    }

    logOut() {
        this._loginService.logOut().subscribe(() => {
           this._router.navigate(['']);
           this.isLogged = false;
        });
    }

    getYoutubeUploadState() {
        this._youtubeService.getStateUpload().subscribe((data) => {
            if (data === 'STOPPED') {
                this.youtubeUploading = false;
            } else if (data === 'UPLOAD_IN_PROGRESS') {
                this.youtubeUploading = true;
            }
        });
    }

    listenYoutubeUpload() {
        this._genericDataService.changeEmittedSubject('youtube-upload').subscribe((data) => {
            if (data === 'FINISHED') {
                this.youtubeUploading = false;
            } else if (data === 'UPLOAD_IN_PROGRESS') {
                this.youtubeUploading = true;
            }
        });
    }

    openDialog(): void {
        const dialogRef = this.dialog.open(DialogGetIpComponent, {
          width: '80%'
        });

        dialogRef.afterClosed().subscribe(result => {
          console.log('The dialog was closed');
        });
      }

}

@Component({
    selector: 'app-dialog-get-ip',
    templateUrl: 'dialog-get-ip.html',
    styleUrls: ['./app.component.css']
  })
  export class DialogGetIpComponent {

    addresses: string[] = [];

    constructor(
        public dialogRef: MatDialogRef<DialogGetIpComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private recordStateService: RecordStateService) {}

    onNoClick(): void {
        this.dialogRef.close();
    }

    getLocalIp() {
        this.recordStateService.getLocalIp().subscribe((data) => {
            this.addresses = data;
        });
    }

    getAllNetworkInterfaces() {
        this.recordStateService.getAllNetworkInterfaces().subscribe((data) => {
            this.addresses = data;
        });
    }

  }
