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

        this._localVideoService.cutVideo(this.fileName).then((cutted) => {
            this.cuttedVideo = cutted;
            this._localVideoService.mergeVideo(this.newNameFile, this.formatContainer)
            .then((merged) => {
                this.mergedVideo = true;
            });
        });

    }

    ngOnInit() {
        this.cutAndMerge();
    }

    ngOnDestroy() {

    }

}
