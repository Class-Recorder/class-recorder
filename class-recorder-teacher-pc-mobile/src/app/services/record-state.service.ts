import { Injectable } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ServerConnectionService } from './server-connection.service';
import { TokenService } from './token.service';

@Injectable()
export class RecordStateService {

    constructor(private _http: Http,
        private serverConnectionService: ServerConnectionService, 
        private tokenService: TokenService) {}

    public getCurrentState(): Observable<string> {
        const headers = new Headers({
            'Authorization': 'Basic ' + this.tokenService.token,
        });
        const options = new RequestOptions({headers: headers});
        let baseUrl = this.serverConnectionService.getBaseUrl();


        const url = baseUrl + '/api/recording/currentState';
        return this._http.get(url, options).pipe(map(res => res.text()));
    }

}
