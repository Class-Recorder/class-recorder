import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { WebsocketRecordService } from './WebSocketRecordService';
import { ServerConnectionService } from '../server-connection.service';

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

    constructor(private wsService: WebsocketRecordService,
        private serverConnectionService: ServerConnectionService) 
    {
        let baseUrl: string = this.serverConnectionService.getBaseUrl();
        baseUrl = baseUrl.replace('http://', 'ws://');
        let WS_URL = `${baseUrl}/recordpc`;
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
