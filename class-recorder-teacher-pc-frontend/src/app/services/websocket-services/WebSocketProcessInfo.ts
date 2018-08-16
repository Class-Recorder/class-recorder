import { Injectable } from '@angular/core';
import { Observable} from 'rxjs';
import { map } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { WebsocketProcessInfoService } from './WebSocketProcessInfoService';
import { environment } from '../../../environments/environment';

@Injectable()
export class WebSocketProcessInfo {
    public messages: Subject<string>;

    constructor(wsService: WebsocketProcessInfoService) {
        let WS_URL;
        console.log(environment);
        if(environment.production) {
            WS_URL = `ws://${document.location.host}/process/info`;
        }
        else {
            WS_URL = `${environment.webSocketUrl}/process/info`;
        }
        console.log(WS_URL);
        this.messages = <Subject<string>>wsService
            .connect(WS_URL)
            .pipe(
                map((response: MessageEvent): string => {
                    const data = response.data;
                    return data;
                })
            );
    }
}
