import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { WebsocketRecordService } from './WebSocketRecordService';
import { environment } from '../../../environments/environment';

export class WebSocketRecordMessage {
    action: string;
    ffmpegContainerFormat?: string;
    frameRate?: number;
    videoName?: string;
}

export class WebSocketRecordMessageServer {
    isError: boolean;
    message: string;
}

@Injectable()
export class WebSocketRecord {
    public messages: Subject<string>;

    constructor(private wsService: WebsocketRecordService) {
        let WS_URL;
        console.log(environment);
        if (environment.production) {
            WS_URL = `ws://${document.location.host}/recordpc`;
        } else {
            WS_URL = `${environment.webSocketUrl}/recordpc`;
        }
        this.messages = <Subject<string>>wsService
            .connect(WS_URL)
            .pipe(map((response: MessageEvent): string => {
                const data = response.data;
                return data;
            })
        );
    }

    sendMessage(msg: WebSocketRecordMessage) {
        this.wsService.sendMessage(msg);
    }
}
