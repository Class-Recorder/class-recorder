import { Component, OnInit } from '@angular/core';
import { LocalVideo } from '../../classes/LocalVideo';
import { LocalVideoService } from '../../services/local-video.service';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-mycourse-localvideos',
    templateUrl: './mycourse-localvideos.component.html',
    styleUrls: ['./mycourse-localvideos.component.css']
})
export class MycourseLocalvideosComponent implements OnInit {

    localVideos: LocalVideo[];
    courseId: number;

    constructor(
        private _localVideoService: LocalVideoService,
        private activatedRoute: ActivatedRoute) { }

    ngOnInit() {
        this.activatedRoute.params.subscribe(params => {
            this.courseId = params['id'];
        })
        this._localVideoService.getLocalVideos().subscribe((data) => {
            this.localVideos = data;
        })
    }

}
