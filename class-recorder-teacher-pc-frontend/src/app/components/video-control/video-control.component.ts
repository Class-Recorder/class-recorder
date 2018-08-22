import { Component, OnInit } from '@angular/core';
import { WebSocketRecord, WebSocketRecordMessageServer } from '../../services/websocket-services/WebSocketRecord';
import { RecordStateService } from '../../services/record-state.service';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';
import { Router } from '@angular/router/src/router';
import { Location } from '@angular/common';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-video-control',
  templateUrl: './video-control.component.html',
  styleUrls: ['./video-control.component.css']
})
export class VideoControlComponent implements OnInit {

    state: string;

  constructor(private _wsRecordService: WebSocketRecord,
        private _recordStateService: RecordStateService,
        private _genericDataService: GenericDataBindingService,
        private _location: Location) { }

    ngOnInit() {
        this._genericDataService.changeEmittedSubject('get-recording-state').subscribe(() => {
            this.initState();
        })
    }

    initState() {
        this._recordStateService.getCurrentState().subscribe((stateData) => {
            this.state = stateData;
        });
        this._wsRecordService.messages.subscribe((message) => {
            let messageFromServer: WebSocketRecordMessageServer = JSON.parse(message);
            console.log(messageFromServer);
            if(messageFromServer.isError) {
                alert(messageFromServer.message);
            }
            this._recordStateService.getCurrentState().subscribe((stateData) => {
                this.state = stateData;
                if (stateData === 'Recording') {
                    this._location.back();
                }
                if (stateData === 'Stopped') {
                }
            });
        });
    }


    pauseRecord() {
        this._wsRecordService.sendMessage({action: 'pause'});
    }

    stopRecord() {
        this._wsRecordService.sendMessage({action: 'stop'});
    }

    continueRecord() {
        this._wsRecordService.sendMessage({action: 'continue'});
    }

}
