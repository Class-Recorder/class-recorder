import { Injectable, OnInit } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { User } from '../classes/user/User';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class LoginService {

    isTeacher = false;
    isLogged = false;
    user: User;

    constructor(private http: Http) {
        this.reqIsLogged();
    }

    reqIsLogged() {

        const headers = new Headers({
            'X-Requested-With': 'XMLHttpRequest'
        });

        const options = new RequestOptions({headers});

        this.http.get('/api/logIn', options).subscribe(
            response => this.processLogInResponse(response),
            error => {
                if (error.status !== 401) {
                    console.error('Error when asking if logged: ' + JSON.stringify(error));
                }
            }
        );
    }

    private processLogInResponse(response) {
        this.user = response.json();
        this.isTeacher = this.user.roles.indexOf('ROLE_TEACHER') !== -1;
        this.isLogged = true;
    }

    logIn(email: string, pass: string) {

        const userPass = email + ':' + pass;

        const headers = new Headers({
            'Authorization': 'Basic ' + utf8_to_b64(userPass),
            'X-Requested-With': 'XMLHttpRequest'
        });

        const options = new RequestOptions({headers});

        return this.http.get('/api/logIn', options).map(
            response => {
                this.processLogInResponse(response);
                return this.user;
            }
        );
    }

    logOut() {

        return this.http.get('/api/logOut').map(
            response => {
                this.isTeacher = false;
                this.isLogged = false;
                this.user = null;
                return response;
            }
        );
    }

    getLoggedUser(): User {
        return this.user;
    }

    isUserTeacher(): boolean {
        return this.isTeacher;
    }
}

function utf8_to_b64(str) {
    return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
        return String.fromCharCode(<any>'0x' + p1);
    }));
}
