import { Component, OnInit } from '@angular/core';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';

@Component({
    selector: 'app-console-component',
    templateUrl: './console-component.component.html',
    styleUrls: ['./console-component.component.css']
})
export class ConsoleComponentComponent implements OnInit {
    
    output: string;

    constructor(private _processWebSocket: WebSocketProcessInfo) { }

    ngOnInit() {
        this._processWebSocket.messages.subscribe((outputData) => {
            this.output += outputData + "\n";
        });
    }

}
