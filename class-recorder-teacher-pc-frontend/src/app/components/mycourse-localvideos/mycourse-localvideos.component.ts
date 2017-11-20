import { Component, OnInit } from '@angular/core';
import { LocalVideo } from '../../classes/LocalVideo';
import { LocalVideoService } from '../../services/local-video.service';

@Component({
    selector: 'app-mycourse-localvideos',
    templateUrl: './mycourse-localvideos.component.html',
    styleUrls: ['./mycourse-localvideos.component.css']
})
export class MycourseLocalvideosComponent implements OnInit {

    localVideos: LocalVideo[];

    constructor(private _localVideoService: LocalVideoService) { }

    ngOnInit() {
        this._localVideoService.getLocalVideos().subscribe((data) => {
            this.localVideos = data;
        })
    }

}
