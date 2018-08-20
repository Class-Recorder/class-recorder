import { Component, OnInit } from '@angular/core';
import { GenericDataBindingService, CutVideoInfo } from '../../services/bind-services/generic-data-binding.service';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { Subscription } from 'rxjs';
import { LocalVideoService } from '../../services/local-video.service';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';

@Component({
    selector: 'app-cut-video-progress',
    templateUrl: './cut-video-progress.component.html',
    styleUrls: ['./cut-video-progress.component.css']
})
export class CutVideoProgressComponent implements OnInit, OnDestroy {

    videoName: string;
    newNameFile: string;
    formatContainer: string;

    cuttedVideo: boolean;
    mergedVideo: boolean;
    output: string;

    consoleSub: Subscription;
    numEnds: number;

    subscription: any;

    constructor(private _localVideoService: LocalVideoService,
                private _genericDataService: GenericDataBindingService,
                private _processWebSocket: WebSocketProcessInfo) {
    }

    async receiveCutInfo() {
        this.cuttedVideo = false;
        this.mergedVideo = false;
        const data: CutVideoInfo = await this._genericDataService
            .changeEmitted('new-file-cutted-video').getValue();

        console.log(data);

        this.newNameFile = data.newNameFile;
        this.formatContainer = data.containerFormat;

        this.videoName = await this._genericDataService
            .changeEmitted('file-to-cut').getValue();

        return true;
    }
    cutAndMerge() {
        this.receiveCutInfo().then((received) => {

            this.subscription = this._processWebSocket.messages.subscribe((outputData: string) => {
                this.output += outputData + '\n';
                if (outputData === 'end' && this.numEnds < 1) {
                    this.numEnds++;
                }
                if (this.numEnds === 1) {
                    this.numEnds++;
                    this.cuttedVideo = true;
                    this._localVideoService.mergeVideo(this.newNameFile, this.formatContainer)
                    .then((merged) => {
                        this.mergedVideo = merged;
                    })
                    .catch((error) => {
                        if (error.status === 503) {
                            alert('Ffmpeg is working');
                        } else if (error.status === 400) {
                            alert('The file doesn\'t exist');
                        } else if (error.status === 409) {
                            alert('No video was cutted');
                        } else if (error.status === 412) {
                            alert('There\'s no files to merge');
                        } else {
                            alert('Internal Server error');
                        }
                    });
                }
            });

            this._localVideoService.cutVideo(this.videoName, this.newNameFile, this.formatContainer)
            .catch((error) => {
                if (error.status === 404) {
                    alert('There\'s no videos on server');
                } else if (error.status === 409) {
                    alert('Video actually exists');
                } else {
                    alert('Internal Server Error');
                }
            });
        });
    }

    ngOnInit() {
        this.numEnds = 0;
        this.output = '';
        this.cutAndMerge();
    }

    ngOnDestroy() {
    }

}
