import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { LocalVideo } from '../classes/LocalVideo'
import 'rxjs/add/operator/map';

@Injectable()
export class LocalVideoService {

    constructor(private _http: Http) {}

    getLocalVideos(): Observable<LocalVideo[]> {
        let url = "/api/getLocalVideos"
        return this._http.get(url).map(res => res.json());
    }
}
