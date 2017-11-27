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
    subsnewFile: Subscription;
    subsFileToCut: Subscription;


    cuttedVideo: boolean;
    mergedVideo: boolean;

    constructor(private _genericBindingService: GenericDataBindingService,
                private _localVideoService: LocalVideoService, 
                private _genericDataService: GenericDataBindingService) { }

              
    ngOnInit() {
        this.subsnewFile = this._genericBindingService.changeEmitted('new-file-cutted-video').subscribe((data) => {
            this.newFileName = data;
            console.log(this.newFileName);
            this.subsFileToCut = this._genericBindingService.changeEmitted('file-to-cut').subscribe((data) => {
                this.fileName = data;
                console.log(this.cuttedVideo);
                console.log(this.fileName);
                this._localVideoService.cutVideo(this.fileName).subscribe();
                this._genericDataService.changeEmitted('console-output-wsocket').subscribe(outputData => {
                    if(outputData === 'end'){
                        this.cuttedVideo = true;
                        this._localVideoService.mergeVideo(this.newFileName).subscribe((merged) => {
                            if(merged){
                                //
                            }
                        })
                    }
                    else if(outputData = 'start'){
                        this.cuttedVideo = false;
                    }
                    
                })
            })
        })
    }

    ngOnDestroy(){
        this.subsnewFile.unsubscribe();
        this.subsFileToCut.unsubscribe();
    }

}
