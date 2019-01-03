import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { RecordStateService } from 'src/app/services/record-state.service';
import { interval, Subscription } from 'rxjs';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
    selector: 'app-video-chrono',
    templateUrl: './video-chrono.component.html',
    styleUrls: ['./video-chrono.component.css']
})
export class VideoChronoComponent implements OnInit, OnDestroy {

    @Input()
    state: string;

    timeRecording: string;

    intervalObserver: Subscription;

    constructor(private _recordStateService: RecordStateService) {
    }

    ngOnInit() {
        if (this.state === 'Recording' || this.state === 'paused') {
            this.intervalObserver = interval(1000).subscribe(() => {
                this._recordStateService.getRecordTime().subscribe((time) => {
                    this.timeRecording = time;
                });
            });
        }
    }

    ngOnDestroy(): void {
        this.intervalObserver.unsubscribe();
    }
}
