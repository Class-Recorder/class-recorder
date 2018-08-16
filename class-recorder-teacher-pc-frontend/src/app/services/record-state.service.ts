import { Injectable, OnInit } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class RecordStateService {

    constructor(private _http: Http) {}

    public getCurrentState(): Observable<string> {
        const url = '/api/recording/currentState';
        return this._http.get(url).pipe(map(res => res.text()));
    }

}
