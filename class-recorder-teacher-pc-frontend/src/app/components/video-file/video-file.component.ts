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

@Component({
    selector: 'app-video-file',
    templateUrl: './video-file.component.html',
    styleUrls: ['./video-file.component.css']
})
export class VideoFileComponent implements OnInit {

    nameFile: string;
    data: any;

    localVideo: LocalVideo;
    videoCuts: string;
    modifiedCuts: boolean;

    constructor(
        private activatedRoute: ActivatedRoute,
        private _localVideoService: LocalVideoService,
        private _processWebSocket: WebSocketProcessInfo,
        private _snackBar: MatSnackBar,
        public  dialog: MatDialog,
        private _genericBindingService: GenericDataBindingService,
        private _router: Router
    ) { }

    ngOnInit() {
        this.activatedRoute.params.subscribe(params => {
            this.nameFile = params['name'];
            this._localVideoService.getLocalVideoByName(this.nameFile).subscribe((localVideoInfo) => {
                this.localVideo = localVideoInfo;
                this._localVideoService.getCutFile(this.localVideo.urlApiLocalCuts).subscribe((cutsInfo) => {
                    this.videoCuts = JSON.stringify(cutsInfo, null, 4);
                });
            });
        });
    }

    saveCuts() {
        const newVideoCut: VideoCutInfo = JSON.parse(this.videoCuts);
        this._localVideoService.postCutFile(this.nameFile, newVideoCut).subscribe(() => {
            this.modifiedCuts = false;
        });
    }

    openDialog(): void {
        const dialogRef = this.dialog.open(VideoCutDialogComponent, {
          width: '80%',
          data: {
              newNameFile: '',
              containerFormat: ''
          }
        });

        dialogRef.afterClosed().subscribe(result => {
            console.log('The dialog was closed');
            this.data = result;
            if (this.data.newNameFile !== null && this.data.newNameFile !== ''
            && this.data.newNameFile !== undefined && this.data.containerFormat !== null
            && this.data.containerFormat !== undefined && this.data.containerFormat !== '') {
                this._genericBindingService.emitChange('new-file-cutted-video', this.data);
                this._genericBindingService.emitChange('file-to-cut', this.nameFile);
                this._router.navigate(['cut-video-progress']);
            }
        });
      }

    numLinesString(str: string): number {
        return str.split(/\r\n|\r|\n/).length;
    }

    onCutModified() {
        this.modifiedCuts = true;
    }

}

@Component({
    selector: 'app-video-cut-dialog',
    templateUrl: 'video-cut-dialog.html',
  })
  export class VideoCutDialogComponent {

    containers: string[];

    constructor(
    public dialogRef: MatDialogRef<VideoCutDialogComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: CutVideoInfo) {
        this.containers = [];
        for (const format in FfmpegContainerFormat) {
            if (isNaN(Number(format))) {
                this.containers.push(format);
            }
        }
    }

    onNoClick(): void {
      this.dialogRef.close();
    }

    enterPressed() {
        document.getElementById('ok-button').click();
    }

  }