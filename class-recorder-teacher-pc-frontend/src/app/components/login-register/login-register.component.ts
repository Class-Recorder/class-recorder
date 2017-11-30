import { Component, OnInit } from '@angular/core';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';

@Component({
    selector: 'app-login-register',
    templateUrl: './login-register.component.html',
    styleUrls: ['./login-register.component.css']
})
export class LoginRegisterComponent implements OnInit {

    output: string;

    constructor() { }

    ngOnInit() {}

}
