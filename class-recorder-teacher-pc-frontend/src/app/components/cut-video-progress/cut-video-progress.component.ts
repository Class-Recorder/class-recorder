import { Component, OnInit } from '@angular/core';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { Subscription } from 'rxjs/Subscription';
import { LocalVideoService } from '../../services/local-video.service';

@Component({
    selector: 'app-cut-video-progress',
    templateUrl: './cut-video-progress.component.html',
    styleUrls: ['./cut-video-progress.component.css']
})
export class CutVideoProgressComponent implements OnInit, OnDestroy {

    newFileName: string;
    fileName: string;
    subscription: Subscription;
    subscription2: Subscription;

    cuttedVideo: boolean;
    mergedVideo: boolean;

    constructor(private _genericBindingService: GenericDataBindingService,
                private _localVideoService: LocalVideoService) { }

    ngOnInit(): void {
        this.subscription = this._genericBindingService.changeEmitted('new-file-cutted-video').subscribe((data) => {
            this.newFileName = data;
            console.log(this.newFileName);
            this.subscription2 = this._genericBindingService.changeEmitted('file-to-cut').subscribe((data) => {
                this.fileName = data;
                console.log(this.fileName);
                this._localVideoService.cutVideo(this.fileName).subscribe((cutted) => {
                    this.cuttedVideo = true;
                })
            })
        })
    }

    ngOnDestroy(){
        this.subscription.unsubscribe();
    }

}
