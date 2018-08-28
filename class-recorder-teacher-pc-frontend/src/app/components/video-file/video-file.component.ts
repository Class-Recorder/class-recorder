import { Component, OnInit, Inject, Optional } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LocalVideoService } from '../../services/local-video.service';
import { LocalVideo } from '../../classes/LocalVideo';
import { VideoCutInfo } from '../../classes/ffmpeg/VideoCutInfo';
import { FfmpegContainerFormat } from '../../classes/ffmpeg/FfmpegContainerFormat';
import { MatSnackBar, MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';
import { GenericDataBindingService, CutVideoInfo } from '../../services/bind-services/generic-data-binding.service';
import { Router } from '@angular/router';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';
import { FormlyJsonschema } from '@ngx-formly/core/json-schema';
import VideoCutsFields from '../../ngx-formly/VideoCutsFields';

@Component({
    selector: 'app-video-file',
    templateUrl: './video-file.component.html',
    styleUrls: ['./video-file.component.css']
})
export class VideoFileComponent implements OnInit {

    nameFile: string;
    videoName: string;
    data: any;

    localVideo: LocalVideo;
    isMp4: boolean;

    form = new FormGroup({});
    videoCuts: any;
    options: FormlyFormOptions = {};
    fields: FormlyFieldConfig[];

    constructor(
        private activatedRoute: ActivatedRoute,
        private _localVideoService: LocalVideoService,
        private _processWebSocket: WebSocketProcessInfo,
        private _snackBar: MatSnackBar,
        public  dialog: MatDialog,
        private _genericBindingService: GenericDataBindingService,
        private _router: Router,
        private formlyJsonschema: FormlyJsonschema
    ) { }

    ngOnInit() {
        this.fields = VideoCutsFields(this.formlyJsonschema);
        this.activatedRoute.params.subscribe(params => {
            this.nameFile = params['name'];
            this.videoName = this.nameFile.substring(0, this.nameFile.lastIndexOf('.'));
            this.isMp4 = this.nameFile.endsWith('.mp4');
            this._localVideoService.getLocalVideoByName(this.videoName).subscribe((localVideoInfo) => {
                this.localVideo = localVideoInfo;
                this._localVideoService.getCutFile(this.localVideo.urlApiLocalCuts).subscribe((cutsInfo) => {
                    this.videoCuts = cutsInfo;
                });
            });
        });
    }

    openDialog(): void {
        const dialogRef = this.dialog.open(VideoCutDialogComponent, {
          width: '80%',
          data: {
              newNameFile: '',
              containerFormat: this.nameFile.substring(this.nameFile.lastIndexOf('.') + 1, this.nameFile.length)
          }
        });

        dialogRef.afterClosed().subscribe(result => {
            console.log('The dialog was closed');
            this.data = result;
            if (this.data.newNameFile !== null && this.data.newNameFile !== ''
            && this.data.newNameFile !== undefined && this.data.containerFormat !== null
            && this.data.containerFormat !== undefined && this.data.containerFormat !== '') {
                const newVideoCut: VideoCutInfo = this.videoCuts;
                this._localVideoService.postCutFile(this.videoName, newVideoCut).subscribe(() => {
                    this._genericBindingService.emitChangeBehaviorSubject('new-file-cutted-video', this.data);
                    this._genericBindingService.emitChangeBehaviorSubject('file-to-cut', this.videoName);
                    this._router.navigate(['cut-video-progress']);
                });
            }
        });
      }

    numLinesString(str: string): number {
        return str.split(/\r\n|\r|\n/).length;
    }

}

@Component({
    selector: 'app-video-cut-dialog',
    templateUrl: 'video-cut-dialog.html',
  })
  export class VideoCutDialogComponent {

    constructor(
    public dialogRef: MatDialogRef<VideoCutDialogComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: CutVideoInfo) {
    }

    onNoClick(): void {
        console.log(this.data);
        this.dialogRef.close();
    }

    enterPressed() {
        document.getElementById('ok-button').click();
    }

  }
