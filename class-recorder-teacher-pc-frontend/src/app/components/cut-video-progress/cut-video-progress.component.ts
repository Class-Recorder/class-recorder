import { Component, OnInit } from '@angular/core';
import { GenericDataBindingService, CutVideoInfo } from '../../services/bind-services/generic-data-binding.service';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { Subscription } from 'rxjs/Subscription';
import { LocalVideoService } from '../../services/local-video.service';

@Component({
    selector: 'app-cut-video-progress',
    templateUrl: './cut-video-progress.component.html',
    styleUrls: ['./cut-video-progress.component.css']
})
export class CutVideoProgressComponent implements OnInit, OnDestroy {

    fileName: string;
    newNameFile: string;
    formatContainer: string;

    cuttedVideo: boolean;
    mergedVideo: boolean;

    consoleSub: Subscription;

    constructor(private _localVideoService: LocalVideoService,
                private _genericDataService: GenericDataBindingService) {

    }

    async cutAndMerge() {
        this.cuttedVideo = false;
        this.mergedVideo = false;
        const data: CutVideoInfo = await this._genericDataService
        .changeEmitted('new-file-cutted-video').getValue();

        console.log(data);

        this.newNameFile = data.newNameFile;
        this.formatContainer = data.containerFormat;

        this.fileName = await this._genericDataService
        .changeEmitted('file-to-cut').getValue();

        let errorCut = false;
        try {
            this.cuttedVideo = await this._localVideoService.cutVideo(this.fileName, this.newNameFile, this.formatContainer);
        } catch (error) {
            errorCut = true;
            if (error.status === 404) {
                alert('There\'s no videos on server');
            } else if (error.status === 409) {
                alert('Video actually exists');
            } else {
                alert('Internal Server Error');
            }
        }

        if (!errorCut) {
            try {
                this.mergedVideo = await this._localVideoService.mergeVideo(this.newNameFile, this.formatContainer);
            } catch (error) {
                if (error.status === 503) {
                    alert('Ffmpeg is working');
                } else if (error.status === 400) {
                    alert('The file doesn\'t exist');
                } else if (error.status === 409) {
                    alert('The file exists');
                } else {
                    alert('Internal Server error');
                }
            }
        }
    }

    ngOnInit() {
        this.cutAndMerge();
    }

    ngOnDestroy() {

    }

}
