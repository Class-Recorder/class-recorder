import { Component, OnInit, OnDestroy } from "@angular/core";
import { Subscription } from "rxjs";
import { RecordStateService } from "../../../app/services/record-state.service";
import { interval } from "rxjs/observable/interval";

@Component({
    selector: 'record-time-component',
    templateUrl: 'record-time-component.html'
})
export class RecordTimeComponent implements OnInit, OnDestroy {

    state: string;

    timeRecording: string;

    intervalObserver: Subscription;

    constructor(private _recordStateService: RecordStateService) {
    }

    ngOnInit() {
        this._recordStateService.getCurrentState().subscribe((state) => {
            this.state = state;
            console.log(this.state);
            if ((this.state === 'Recording' || this.state === 'paused') && !this.intervalObserver) {
                this.intervalObserver = interval(1000).subscribe(() => {
                    this._recordStateService.getRecordTime().subscribe((time) => {
                        this.timeRecording = time;
                        console.log(this.timeRecording);
                    });
                });
            }
        })
    }

    ngOnDestroy(): void {
        if(this.intervalObserver) {
            this.intervalObserver.unsubscribe();
            this.intervalObserver = undefined;
        }
    }
}