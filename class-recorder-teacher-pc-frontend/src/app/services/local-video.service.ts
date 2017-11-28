import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { LocalVideo } from '../classes/LocalVideo'
import 'rxjs/add/operator/map';
import { VideoCutInfo } from '../classes/ffmpeg/VideoCutInfo';
import { Cut } from '../classes/ffmpeg/Cut';

@Injectable()
export class LocalVideoService {

    constructor(private _http: Http) {}

    public getLocalVideos(): Observable<LocalVideo[]> {
        let url = "/api/getLocalVideos/"
        return this._http.get(url).map(res => res.json());
    }

    public getLocalVideoByName(name: string):Observable<LocalVideo>{
        let url = "/api/getLocalVideos/" + name;
        return this._http.get(url).map(res => res.json());
    }

    public getCutFile(url: string): Observable<Cut>{
        return this._http.get(url).map(res => res.json());
    }

    public postCutFile(fileName: string, videoCutInfo: VideoCutInfo): Observable<boolean>{
        let url = "/api/updateCutFileOf/" + fileName;
        return this._http.post(url, videoCutInfo).map(res => res.json());
    }

    public cutVideo(fileName: string): Observable<boolean>{
        let url = "/api/cutVideo/" + fileName;
        return this._http.get(url).map(res => res.json());
    }

    public mergeVideo(newFileName: string, container: string): Observable<boolean> {
        let url = "/api/mergeVideo/" + newFileName + "/" + container;
        return this._http.post(url, null).map(res => res.json());
    }
}
