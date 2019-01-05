import { Injectable } from "@angular/core";
import { TokenService } from "./token.service";
import { Http, RequestOptions, Headers } from '@angular/http';
import { Observable } from "rxjs";
import { ServerConnectionService } from "./server-connection.service";
import { map } from "rxjs/operators";

@Injectable()
export class SlidesService {

    constructor(private tokenService: TokenService,
        private serverConnectionService: ServerConnectionService,
        private _http: Http) {
    
    }

    public leftSlide(): Observable<string> {
        const headers = new Headers({
            'Authorization': 'Basic ' + this.tokenService.token,
        });
        const options = new RequestOptions({headers: headers});
        let baseUrl = this.serverConnectionService.getBaseUrl();

        const url = baseUrl + '/api/leftSlides';
        return this._http.get(url, options).pipe(map(res => res.text()));
    }

    public rightSlide(): Observable<string> {
        const headers = new Headers({
            'Authorization': 'Basic ' + this.tokenService.token,
        });
        const options = new RequestOptions({headers: headers});
        let baseUrl = this.serverConnectionService.getBaseUrl();

        const url = baseUrl + '/api/rightSlides';
        return this._http.get(url, options).pipe(map(res => res.text()));
    }

}