import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { WebSocketYoutubeProgressService } from "./WebSocketYoutubeProgressService";
import { environment } from "../../../environments/environment";
import { map } from "rxjs/operators";

@Injectable()
export class WebSocketYoutubeProgress {
    public messages: Subject<string>;

    constructor(wsService: WebSocketYoutubeProgressService) {
        let WS_URL;
        console.log(environment);
        if(environment.production) {
            WS_URL = `ws://${document.location.host}/youtube/progress`;
        }
        else {
            WS_URL = `${environment.webSocketUrl}/youtube/progress`;
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