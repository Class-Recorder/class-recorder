import { Component, OnInit, Input } from '@angular/core';
import { WebSocketRecord, WebSocketRecordMessageServer } from '../../services/websocket-services/WebSocketRecord';
import { RecordStateService } from '../../services/record-state.service';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';
import { Location } from '@angular/common';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-video-control',
  templateUrl: './video-control.component.html',
  styleUrls: ['./video-control.component.css']
})
export class VideoControlComponent implements OnInit {

    state: string;

    backgroundColor = 'black';

    @Input()
    teacherId: number;

  constructor(private _wsRecordService: WebSocketRecord,
        private _recordStateService: RecordStateService,
        private _genericDataService: GenericDataBindingService,
        private _themeService: ThemeService,
        private _router: Router) { }

    ngOnInit() {
        this._genericDataService.changeEmittedSubject('get-recording-state').subscribe(() => {
            this.initState();
        });
        let win: any = window;
        win.AngularRecord = this;
    }

    initState() {
        this._recordStateService.getCurrentState().subscribe((stateData) => {
            this.state = stateData;
        });
        this._wsRecordService.messages.subscribe((message) => {
            const messageFromServer: WebSocketRecordMessageServer = JSON.parse(message);
            console.log(messageFromServer);
            if (messageFromServer.isError) {
                alert(messageFromServer.message);
            }
            this._recordStateService.getCurrentState().subscribe((stateData) => {
                this.state = stateData;
                if (this._router.url === '/record-video') {
                    this._router.navigate(['courselist', this.teacherId]);
                }
            });
        });
        console.log(this._themeService.getTheme());
        if (this._themeService.getTheme() === 'indigo-pink') {
            this.backgroundColor = 'white';
        }
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
