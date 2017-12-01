import { Component, OnInit } from '@angular/core';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';
import { Input } from '@angular/core';

@Component({
    selector: 'app-console-component',
    templateUrl: './console-component.component.html',
    styleUrls: ['./console-component.component.css']
})
export class ConsoleComponentComponent implements OnInit {

    @Input()
    output: string;

    constructor(private _processWebSocket: WebSocketProcessInfo,
                private _genericDataService: GenericDataBindingService) { }

    ngOnInit() {
    }

}
