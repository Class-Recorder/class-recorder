import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import { WebsocketService } from './WebSocketService';

const WS_URL = 'ws://localhost:8000/process/info';

@Injectable()
export class WebSocketProcessInfo {
	public messages: Subject<string>;

    constructor(wsService: WebsocketService) {
		this.messages = <Subject<string>>wsService
			.connect(WS_URL)
			.map((response: MessageEvent): string => {
				let data = response.data;
				return data;
			});
	}
}