import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import { WebsocketService } from './WebSocketService';

const CHAT_URL = 'ws://localhost:8000/process/info';

@Injectable()
export class WebSocketProcessInfo {
	public messages: Subject<string>;

    constructor(wsService: WebsocketService) {
		this.messages = <Subject<string>>wsService
			.connect(CHAT_URL)
			.map((response: MessageEvent): string => {
				let data = response.data;
				return data;
			});
	}
}