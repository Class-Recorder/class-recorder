import { Component, OnInit, Inject } from '@angular/core';
import { YoutubeVideo } from '../../classes/Video';
import { YoutubeService } from '../../services/youtube.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';

@Component({
    selector: 'app-mycourse-uploadedvideos',
    templateUrl: './mycourse-uploadedvideos.component.html',
    styleUrls: ['./mycourse-uploadedvideos.component.css']
})
export class MycourseUploadedVideosComponent implements OnInit {

    youtubeVideos: YoutubeVideo[] = [];
    courseId: number;
    currentPage = 0;
    stopScroll;

    constructor(private _youtubeService: YoutubeService,
        private _activatedRoute: ActivatedRoute,
        private dialog: MatDialog) {}

    ngOnInit() {
        this.youtubeVideos = [];
        this.stopScroll = false;
        this._activatedRoute.params.subscribe((params) => {
            this.courseId = params['id'];
            console.log(this.courseId);
            this._youtubeService.getYoutubeVideos(this.courseId, 0).subscribe((data) => {
                this.youtubeVideos = data;
                this.currentPage++;
            });
        });
    }

    openDialog(videoId: number): void {
        this._youtubeService.getYoutubeVideoById(videoId).subscribe((videoData) => {
            let formData: any = {
                videoTitle: videoData.title,
                description: videoData.description,
                tags: videoData.tags.join()
            };
            const dialogRef = this.dialog.open(DialogUpdateYoutubeVideoComponent, {
                width: '80%',
                data: { videoNewInfo: formData }
            });
            dialogRef.afterClosed().subscribe(result => {
                console.log('The dialog was closed');
                if (result !== undefined) {
                    formData = result;
                    console.log(result);
                    this._youtubeService.updateVideo(videoId, formData).subscribe((data) => {
                    this.ngOnInit();
                    console.log(data);
                });
                }
            });
        });
    }

    openDialogDelete(videoId: number) {
        const dialogRef = this.dialog.open(DialogDeleteVideoComponent, {
            width: '80%',
            data: { answer: true }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result !== undefined) {
                console.log(result);
                this._youtubeService.deleteVideo(videoId).subscribe((data) => {
                    this.ngOnInit();
                });
            }
        });
    }

    onScroll() {
        if (this.currentPage > 0 && !this.stopScroll) {
            this._youtubeService.getYoutubeVideos(this.courseId, this.currentPage).subscribe((data: any) => {
                for (const localVideo of data) {
                    this.youtubeVideos.push(localVideo);
                }
                console.log(data);
                this.stopScroll = data.length === 0;
            });
            this.currentPage++;
        }
    }
}

@Component({
        selector: 'app-dialog-update-youtube-video',
        templateUrl: 'dialog-update-youtube-video.html',
})
export class DialogUpdateYoutubeVideoComponent {

    constructor(
        public dialogRef: MatDialogRef<DialogUpdateYoutubeVideoComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    form = new FormGroup({});
    options: FormlyFormOptions = {};
    fields: FormlyFieldConfig[] = [
        {
            key: 'videoTitle',
            type: 'input',
            templateOptions: {
                label: 'Video Title',
                required: true
            }
        },
        {
            key: 'description',
            type: 'textarea',
            templateOptions: {
                label: 'Description',
                required: true,
                rows: 5
            }
        },
        {
            key: 'tags',
            type: 'textarea',
            templateOptions: {
                label: 'Tags (separated by commas)',
                pattern: /^[\w\s]+(?:,[\w\s]*)*$/,
                required: false,
                rows: 2
            },
            validation: {
                messages: {
                    pattern: (error, field: FormlyFieldConfig) => `Tags must be phrases separated by commas`
                },
            }
        }
    ];

    submit() {
        if (this.form.valid) {
            this.dialogRef.close();
        }
    }

}

@Component({
    selector: 'app-dialog-delete-video',
    templateUrl: 'dialog-delete-video.html',
    styleUrls: ['./mycourse-uploadedvideos.component.css']
})
export class DialogDeleteVideoComponent {

    constructor(
        public dialogRef: MatDialogRef<DialogDeleteVideoComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onNoClick(): void {
        this.dialogRef.close();
    }

    onYesClick(): void {
        this.dialogRef.close();
    }
}


