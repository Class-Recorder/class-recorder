import {Injectable } from "@angular/core";
import { Http } from '@angular/http';
import { Observable } from "rxjs/Observable";
import { map } from 'rxjs/operators';

@Injectable()
export class ServerConnectionService {

    private baseUrl: string; 

    constructor(private _http: Http) {
    }

    public getBaseUrl(): string {
        return this.baseUrl;
    }

    connect(ip: string, port: string): Observable<boolean> {
        this.baseUrl = 'http://' + ip + ':' + port; 
        const url = this.baseUrl + '/api/testConnection';
        return this._http.get(url).pipe(map(res => res.json()));
    }
}