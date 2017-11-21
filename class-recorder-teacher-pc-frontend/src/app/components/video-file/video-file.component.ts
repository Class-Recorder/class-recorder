import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LocalVideoService } from '../../services/local-video.service';
import { LocalVideo } from '../../classes/LocalVideo';
import { VideoCutInfo } from '../../classes/ffmpeg/VideoCutInfo'
import { MatSnackBar } from '@angular/material';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';

@Component({
    selector: 'app-video-file',
    templateUrl: './video-file.component.html',
    styleUrls: ['./video-file.component.css']
})
export class VideoFileComponent implements OnInit {

    nameFile: string;
    localVideo: LocalVideo;
    videoCuts: string;
    modifiedCuts: boolean;

    constructor(
        private activatedRoute: ActivatedRoute,
        private _localVideoService: LocalVideoService,
        private _snackBar: MatSnackBar,
        private _processWebSocket: WebSocketProcessInfo
    ) { }

    ngOnInit() {
        this.activatedRoute.params.subscribe(params => {
            this.nameFile = params['name'];
            this._localVideoService.getLocalVideoByName(this.nameFile).subscribe((localVideoInfo) => {
                this.localVideo = localVideoInfo;
                this._localVideoService.getCutFile(this.localVideo.urlApiLocalCuts).subscribe((cutsInfo) => {
                    this.videoCuts = JSON.stringify(cutsInfo, null, 4);
                });
            })
        })
        this._processWebSocket.messages.subscribe((msg) => {
            console.log(msg);
        })  
    }

    saveCuts(){
        let newVideoCut: VideoCutInfo = JSON.parse(this.videoCuts);
        this._localVideoService.postCutFile(this.nameFile, newVideoCut).subscribe(() => {
            this.modifiedCuts = false;
        })
    }

    numLinesString(str: string): number{
        return str.split(/\r\n|\r|\n/).length;
    }

    onCutModified(){
        this.modifiedCuts = true;
    }

}
