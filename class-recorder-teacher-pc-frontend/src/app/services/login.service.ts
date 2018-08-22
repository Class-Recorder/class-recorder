import { Injectable, OnInit } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { User } from '../classes/user/User';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators'

@Injectable()
export class LoginService {

    public isTeacher = false;
    public isLogged = false;
    public user: User;

    constructor(private http: Http) {
    }

    public reqIsLogged = () => new Promise((resolve, reject) => {
        const headers = new Headers({
            'X-Requested-With': 'XMLHttpRequest'
        });

        const options = new RequestOptions({headers});

        this.http.get('/api/logIn', options).subscribe(
            response => {
                console.log(response);
                this.processLogInResponse(response);
                resolve();
            },
            error => {
                if (error.status !== 401) {
                    console.error('Error when asking if logged: ' + JSON.stringify(error));
                    reject();
                }
            }
        );
    });

    private processLogInResponse(response) {
        this.user = response.json();
        this.isTeacher = this.user.roles.indexOf('ROLE_TEACHER') !== -1;
        this.isLogged = true;
    }

    public logIn(email: string, pass: string) {

        const userPass = email + ':' + pass;

        const headers = new Headers({
            'Authorization': 'Basic ' + utf8_to_b64(userPass),
            'X-Requested-With': 'XMLHttpRequest'
        });

        const options = new RequestOptions({headers});

        return this.http.get('/api/logIn', options).pipe(map(
            response => {
                this.processLogInResponse(response);
                return this.user;
            }
        ));
    }

    public logOut() {

        return this.http.get('/api/logOut').pipe(map(
            response => {
                this.isTeacher = false;
                this.isLogged = false;
                this.user = null;
                return response;
            }
        ));
    }

    public getLoggedUser(): User {
        return this.user;
    }

    public isUserTeacher(): boolean {
        return this.isTeacher;
    }
}

function utf8_to_b64(str) {
    return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
        return String.fromCharCode(<any>'0x' + p1);
    }));
}
