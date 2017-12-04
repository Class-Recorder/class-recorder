import { Component, OnInit } from '@angular/core';
import { VideoToRecInfo } from '../../form-classes/video-to-rec-info';
import { FormGroup, AbstractControl } from '@angular/forms';
import { FormBuilder, Validators } from '@angular/forms';
import { ValidatorFn } from '@angular/forms';
import { FfmpegContainerFormat } from '../../classes/ffmpeg/FfmpegContainerFormat';
import { WebSocketRecord, WebSocketRecordMessage } from '../../services/websocket-services/WebSocketRecord';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';

@Component({
        selector: 'app-add-video',
        templateUrl: './add-video.component.html',
        styleUrls: ['./add-video.component.css']
})
export class AddVideoComponent implements OnInit {

    private videoToRecInfo: VideoToRecInfo;
    private formValidator: FormGroup;
    private isValidFormSubmitted: boolean;

    private patternFrameRate = '^(2[5-9]|[3-5][0-9]|60)$';
    private containers: string[];

    constructor(private _formBuilder: FormBuilder,
                private _wsRecordService: WebSocketRecord,
                private _genericDataService: GenericDataBindingService) {
        this.containers = [];
    }

    ngOnInit() {
        this.formValidator = this._formBuilder.group({
            ffmpegContainerFormat : ['', [Validators.required]],
            frameRate : ['', [Validators.required, Validators.pattern(this.patternFrameRate)]],
            videoName: ['', [Validators.required]]
        });
        this.videoToRecInfo = new VideoToRecInfo();
        for (const format in FfmpegContainerFormat) {
            if (isNaN(Number(format))) {
                this.containers.push(format);
            }
        }
    }

    onSubmit() {
        const wsRecordMessage = new WebSocketRecordMessage();
        wsRecordMessage.action = 'recordVideoAndAudio';
        wsRecordMessage.ffmpegContainerFormat = this.videoToRecInfo.ffmpegContainerFormat;
        wsRecordMessage.frameRate = this.videoToRecInfo.frameRate;
        wsRecordMessage.videoName = this.videoToRecInfo.videoName;
        this._wsRecordService.sendMessage(wsRecordMessage);
    }

    onKey(event: any) {
    }

}
