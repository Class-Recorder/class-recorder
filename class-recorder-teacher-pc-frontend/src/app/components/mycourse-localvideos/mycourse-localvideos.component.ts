import { Component, OnInit } from '@angular/core';
import { LocalVideo } from '../../classes/LocalVideo';
import { LocalVideoService } from '../../services/local-video.service';
import { ActivatedRoute } from '@angular/router';
import { RecordStateService } from '../../services/record-state.service';

@Component({
    selector: 'app-mycourse-localvideos',
    templateUrl: './mycourse-localvideos.component.html',
    styleUrls: ['./mycourse-localvideos.component.css']
})
export class MycourseLocalvideosComponent implements OnInit {

    localVideos: LocalVideo[] = [];
    courseId: number;
    currentPage = 0;
    stopScroll: boolean;

    state: string;

    constructor(
        private _localVideoService: LocalVideoService,
        private _activatedRoute: ActivatedRoute,
        private _recordStateService: RecordStateService) { }

    ngOnInit() {
        this.stopScroll = false;
        this.localVideos = [];
        this._activatedRoute.params.subscribe(params => {
            this.courseId = params['id'];
        });
        this._localVideoService.getLocalVideos(0).subscribe((data) => {
            this.localVideos = data;
            this.currentPage++;
        });
        this._recordStateService.getCurrentState().subscribe((stateData) => {
            console.log(stateData);
            this.state = stateData;
        });
    }

    isStopped() {
        return this.state === 'Stopped';
    }

    onScroll() {
        if (this.currentPage > 0 && !this.stopScroll) {
            this._localVideoService.getLocalVideos(this.currentPage).subscribe((data: any) => {
                for (const localVideo of data) {
                    this.localVideos.push(localVideo);
                }
                this.stopScroll = data.length === 0;
            });
            this.currentPage++;
        }
    }

}
