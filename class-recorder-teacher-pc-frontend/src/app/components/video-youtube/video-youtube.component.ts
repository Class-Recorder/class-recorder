import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { YoutubeService } from '../../services/youtube.service';
import { YoutubeVideo } from '../../classes/Video';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
    selector: 'app-video-youtube',
    templateUrl: './video-youtube.component.html',
    styleUrls: ['./video-youtube.component.css']
})
export class VideoYoutubeComponent implements OnInit {

    videoId: number;
    videoInfo: YoutubeVideo;
    videoUrl: SafeUrl;

    constructor(private activatedRoute: ActivatedRoute,
        private youtubeService: YoutubeService,
        private sanitizer: DomSanitizer) { }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe((params) => {
            this.videoId = params['id'];
            this.youtubeService.getYoutubeVideoById(this.videoId).subscribe((videoInfo) => {
                this.videoInfo = videoInfo;
                const link = 'https://www.youtube.com/embed/' + this.videoInfo.youtubeId;
                this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(link);
            });
        });
    }
}
