import { Component, OnInit } from '@angular/core';
import { WebSocketYoutubeProgress } from '../../services/websocket-services/WebSocketYoutubeProgress';
import { YoutubeService } from '../../services/youtube.service';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';


@Component({
    selector: 'app-upload-video-youtube-progress',
    templateUrl: './upload-video-youtube-progress.component.html',
    styleUrls: ['./upload-video-youtube-progress.component.css']
})
export class UploadVideoYoutubeProgressComponent implements OnInit {

    notUploading: boolean;
    progress: number;
    state: string;

    constructor(private _progressYoutubeWs: WebSocketYoutubeProgress,
        private _youtubeService: YoutubeService,
        private _genericBindingService: GenericDataBindingService) {
    }

    ngOnInit() {
        this._youtubeService.getStateUpload().subscribe((state) => {
            this.state = state;
            if (state === 'STOPPED') {
                this.notUploading = true;
                this._youtubeService.getYoutubeOAuthUrl().subscribe((oAuthUrl) => {
                    window.open(oAuthUrl);
                });
            } else if (state === 'UPLOAD_IN_PROGRESS') {
                this.notUploading = false;
                this._youtubeService.getYoutubeProgress().subscribe((progress) => {
                    console.log(state);
                    this.progress = parseFloat(progress) * 100;
                });
            }
        });
        this._progressYoutubeWs.messages.subscribe((data) => {
            if (data === 'UPLOAD_IN_PROGRESS') {
                this._genericBindingService.emitChangeSubject('youtube-upload', data);
                this.notUploading = false;
                this.progress = 0;
                this.state = data;
            }
            if (data.startsWith('Percentage: ')) {
                const currentPercentage: number = parseFloat(data.replace('Percentage: ', '')) * 100;
                console.log(currentPercentage);
                this.progress = currentPercentage;
            }
            if (data === 'FINISHED') {
                this._genericBindingService.emitChangeSubject('youtube-upload', data);
                this.progress = 100;
                this.notUploading = true;
                this.state = data;
            }
            console.log(data);
        });
    }
}
