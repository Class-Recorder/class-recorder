import { Component, ViewChild } from '@angular/core';
import { WebSocketRecordMessageServer, WebSocketRecord, WebSocketRecordMessage } from '../../../app/services/websocket-services/WebSocketRecord';
import { RecordStateService } from '../../../app/services/record-state.service';
import { ModalController, Platform, AlertController } from 'ionic-angular';
import { AddVideoModal } from '../../../app/modals/add-video/add-video';
import { LoginService } from '../../../app/services/login.service';
import { MediaObject, Media } from '@ionic-native/media';
import { File } from '@ionic-native/file';
import { UploadAudioService } from '../../../app/services/upload-audio.service';
import { BackgroundMode } from '@ionic-native/background-mode';
import { SpinnerDialog } from '@ionic-native/spinner-dialog';
import { RecordTimeComponent } from '../record-time/record-time-component';
import { Insomnia } from '@ionic-native/insomnia';

@Component({
    selector: 'record-state-component',
    templateUrl: 'record-state-component.html'
})
export class RecordStateComponent {

    state: string;

    //Audio
    recording: boolean = false;
    paused: boolean = false;
    filePath: string;
    fileName: string;
    audio: MediaObject;

    videoName: string;
    containerFormat: string;

    @ViewChild('yourChild') timer: RecordTimeComponent;

    constructor(private wsRecordService: WebSocketRecord,
        private recordStateService: RecordStateService,
        public modalCtrl: ModalController,
        private loginService: LoginService,
        private media: Media,
        private file: File,
        public platform: Platform,
        private uploadAudioService: UploadAudioService,
        private insomniaService: Insomnia,
        private backgroundService: BackgroundMode,
        private backgroundMode: BackgroundMode,
        private alertCtrl: AlertController,
        private spinnerDialog: SpinnerDialog) { }

    ngOnInit() {
        if(this.loginService.isLogged) {
            this.initState();
        }
    }

    initState() {
        this.recordStateService.getCurrentState().subscribe((stateData) => {
            this.state = stateData;
        });
        this.wsRecordService.messages.subscribe((message) => {
            const messageFromServer: WebSocketRecordMessageServer = JSON.parse(message);
            console.log(messageFromServer);
            if (messageFromServer.isError) {
                let alert = this.alertCtrl.create({
                    title: 'Error',
                    subTitle: messageFromServer.message,
                    buttons: ['Ok']
                });
                alert.present();
            }
            this.recordStateService.getCurrentState().subscribe((stateData) => {
                if(stateData === 'Recording' && !this.paused) {
                    this.startAudioRecord();
                    this.timer.ngOnInit();
                }
                else if(stateData === 'Recording' && this.paused){
                    this.paused = true;
                }
                else if(stateData === 'Stopped') {
                    this.paused = false;
                    this.timer.ngOnDestroy();
                    this.stopAudioRecord();
                    if(this.audio !== undefined) {
                        this.spinnerDialog.show('Uploading audio', 'Recording audio is uploading'); 
                        this.uploadAudioService.uploadAudio(this.filePath, this.containerFormat, this.videoName).then((data) => {
                            this.spinnerDialog.hide()
                            this.backgroundMode.disable();
                        }).catch(error => {
                            this.spinnerDialog.hide();
                            let alert = this.alertCtrl.create({
                                title: 'Error',
                                subTitle: error.message,
                                buttons: ['Ok']
                            });
                            alert.present(); 
                        });
                    }
                }
                else if(stateData === 'Paused') {
                    this.paused = true;
                }
                this.state = stateData;
            });
        });
    }

    startRecord() {
        let profileModal = this.modalCtrl.create(AddVideoModal);
        profileModal.onDidDismiss(data => {
            if(data !== undefined) {
                this.backgroundMode.enable();
                const wsRecordMessage = new WebSocketRecordMessage();
                this.videoName = data.videoName;
                this.containerFormat = data.ffmpegContainerFormat;
                wsRecordMessage.action = 'recordVideo';
                wsRecordMessage.ffmpegContainerFormat = data.ffmpegContainerFormat;
                wsRecordMessage.frameRate = data.frameRate;
                wsRecordMessage.videoName = data.videoName;
                console.log(wsRecordMessage);
                this.wsRecordService.sendMessage(wsRecordMessage);
            }
        });
        profileModal.present();  
    }


    pauseRecord() {
        this.wsRecordService.sendMessage({action: 'pause'});
    }

    stopRecord() {
        this.wsRecordService.sendMessage({action: 'stop'});
    }

    continueRecord() {
        this.wsRecordService.sendMessage({action: 'continue'});
    }

    startAudioRecord() {
        if(this.platform.is('cordova')) {
            this.insomniaService.keepAwake().then();
            this.backgroundMode.enable();
            if (this.platform.is('ios')) {
                this.fileName = 'class-recorder-record'+new Date().getDate()+new Date().getMonth()+new Date().getFullYear()+new Date().getHours()+new Date().getMinutes()+new Date().getSeconds()+'.m4a';
                this.filePath = this.file.documentsDirectory.replace(/file:\/\//g, '') + this.fileName;
                this.audio = this.media.create(this.filePath);
            } else if (this.platform.is('android')) {
                this.fileName = 'class-recorder-record'+new Date().getDate()+new Date().getMonth()+new Date().getFullYear()+new Date().getHours()+new Date().getMinutes()+new Date().getSeconds()+'.mp3';
                this.filePath = this.file.externalDataDirectory.replace(/file:\/\//g, '') + this.fileName;
                this.audio = this.media.create(this.filePath);
            }
            this.audio.setRate(256);
            this.audio.startRecord();
            this.recording = true;
        }
    }

    stopAudioRecord() {
        if(this.audio !== undefined && this.platform.is('cordova')) {
            this.insomniaService.allowSleepAgain().then();
            this.audio.stopRecord();
            this.backgroundMode.disable();
            console.log(this.filePath);
            this.recording = false;
        }
    }

}