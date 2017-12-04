import { Component, OnInit } from '@angular/core';
import { WebSocketRecord } from '../../services/websocket-services/WebSocketRecord';
import { RecordStateService } from '../../services/record-state.service';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';
import { Router } from '@angular/router/src/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-video-control',
  templateUrl: './video-control.component.html',
  styleUrls: ['./video-control.component.css']
})
export class VideoControlComponent implements OnInit {

    state: string;
    started: boolean;

  constructor(private _wsRecordService: WebSocketRecord,
        private _recordStateService: RecordStateService,
        private _genericDataService: GenericDataBindingService,
        private _location: Location) { }

    ngOnInit() {
        this._wsRecordService.messages.subscribe((message) => {
            console.log(message);
            this._recordStateService.getCurrentState().subscribe((stateData) => {
                this.state = stateData;
                if (stateData === 'Recording' && !this.started) {
                    this._location.back();
                    this.started = true;
                }
                if (stateData === 'Stopped') {
                    this.started = false;
                }
            });
        });
        this._recordStateService.getCurrentState().subscribe((stateData) => {
            this.state = stateData;
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
