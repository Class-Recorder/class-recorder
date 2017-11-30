import { Injectable } from '@angular/core';
import { Observable} from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { WebsocketService } from './WebSocketService';

const WS_URL = 'ws://localhost:8000/process/info';

@Injectable()
export class WebSocketProcessInfo {
    public messages: Subject<string>;

    constructor(wsService: WebsocketService) {
        this.messages = <Subject<string>>wsService
            .connect(WS_URL)
            .map((response: MessageEvent): string => {
                const data = response.data;
                return data;
            });
    }
}
