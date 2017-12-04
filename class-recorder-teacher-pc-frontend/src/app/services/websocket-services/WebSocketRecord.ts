import { Injectable } from '@angular/core';
import { Observable} from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { WebsocketRecordService } from './WebSocketRecordService';

const WS_URL = 'ws://localhost:8000/recordpc';

export class WebSocketRecordMessage {
    action: string;
    ffmpegContainerFormat?: string;
    frameRate?: number;
    videoName?: string;
}

@Injectable()
export class WebSocketRecord {
    public messages: Subject<string>;

    constructor(private wsService: WebsocketRecordService) {
        this.messages = <Subject<string>>wsService
            .connect(WS_URL)
            .map((response: MessageEvent): string => {
                const data = response.data;
                return data;
            });
    }

    sendMessage(msg: WebSocketRecordMessage) {
        this.wsService.sendMessage(msg);
    }
}
