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

    data: any;
    fileName: string;
    subsnewFile: Subscription;
    subsFileToCut: Subscription;
    subsMerge: Subscription;


    cuttedVideo: boolean;
    mergedVideo: boolean;

    constructor(private _genericBindingService: GenericDataBindingService,
                private _localVideoService: LocalVideoService, 
                private _genericDataService: GenericDataBindingService) { }

    //Callback hell
    //TODO: Refactor call services       
    ngOnInit() {
        this.subsnewFile = this._genericBindingService.changeEmitted('new-file-cutted-video').subscribe((data) => {
            this.data = data;
            console.log(this.data);
            this.subsFileToCut = this._genericBindingService.changeEmitted('file-to-cut').subscribe((data) => {
                this.fileName = data;
                console.log(this.cuttedVideo);
                console.log(this.fileName);
                this._localVideoService.cutVideo(this.fileName).subscribe();
                this._genericDataService.changeEmitted('console-output-wsocket').subscribe(outputData => {
                    if(outputData === 'end'){
                        this.cuttedVideo = true;
                        this.subsMerge = this._localVideoService.mergeVideo(this.data.newNameFile, this.data.containerFormat).subscribe((merged) => {
                            if(merged){
                                this.mergedVideo = true;
                            }
                        })
                    }
                    else if(outputData = 'start'){
                        this.cuttedVideo = false;
                        this.mergedVideo = false;
                    }
                    
                })
            })
        })
    }

    ngOnDestroy(){
        this.subsnewFile.unsubscribe();
        this.subsFileToCut.unsubscribe();
        this.subsMerge.unsubscribe();
    }

}
