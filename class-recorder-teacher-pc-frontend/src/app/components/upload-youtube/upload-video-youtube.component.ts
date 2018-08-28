import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';
import { YoutubeService } from '../../services/youtube.service';
import UploadYoutubeVideoFields from '../../ngx-formly/UploadYoutubeVideoFields';

@Component({
        selector: 'app-upload-video-youtube',
        templateUrl: './upload-video-youtube.component.html',
        styleUrls: ['./upload-video-youtube.component.css']
})
export class UploadVideoYoutubeComponent implements OnInit {

    fileName: string;
    courseId: number;
    containerFormat: string;

    form = new FormGroup({});
    model: any = {
        tags: ''
    };
    options: FormlyFormOptions = {};

    fields: FormlyFieldConfig[] = UploadYoutubeVideoFields;


    constructor(private activatedRoute: ActivatedRoute,
        private youtubeService: YoutubeService,
        private router: Router) {

    }

    ngOnInit() {
        this.activatedRoute.params.subscribe(params => {
            this.fileName = params['filename'].substring(0, params['filename'].lastIndexOf('.'));
            console.log(this.fileName);
            this.courseId = params['courseid'];
            console.log(this.courseId);
            this.containerFormat = params['filename'].substring(params['filename'].lastIndexOf('.') + 1, params['filename'].length);
            console.log(this.containerFormat);
        });
    }

    submit() {
        if (this.form.valid) {
            console.log(this.model);
            this.youtubeService.postVideo(this.courseId, this.fileName, this.containerFormat, this.model).subscribe((videoInfo: any) => {
                console.log(videoInfo);
                this.router.navigate(['uploadvideo-progress']);
            });
        }
    }

}
