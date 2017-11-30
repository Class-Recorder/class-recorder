import { Component, OnInit } from '@angular/core';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';

@Component({
    selector: 'app-console-component',
    templateUrl: './console-component.component.html',
    styleUrls: ['./console-component.component.css']
})
export class ConsoleComponentComponent implements OnInit {

    output: string;

    constructor(private _processWebSocket: WebSocketProcessInfo,
                private _genericDataService: GenericDataBindingService) { }

    ngOnInit() {
        this._genericDataService.emitChange('console-output-wsocket', 'start');
        this._processWebSocket.messages.subscribe((outputData: string) => {
            if (outputData === 'end') {
                this._genericDataService.emitChange('console-output-wsocket', outputData);
            } else {
                this.output += outputData + '\n';
            }
        });
    }

}
