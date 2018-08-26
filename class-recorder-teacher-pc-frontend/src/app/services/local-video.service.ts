import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { timeout, map } from 'rxjs/operators';
import { LocalVideo } from '../classes/LocalVideo';
import { VideoCutInfo } from '../classes/ffmpeg/VideoCutInfo';
import { Cut } from '../classes/ffmpeg/Cut';

@Injectable()
export class LocalVideoService {

    constructor(private _http: Http) {}

    public getLocalVideos(page: number): Observable<LocalVideo[]> {
        const url = '/api/getLocalVideos/' + '?page=' + page;
        return this._http.get(url).pipe(map(res => res.json()));
    }

    public getLocalVideoByName(name: string): Observable<LocalVideo> {
        const url = '/api/getLocalVideos/' + name;
        return this._http.get(url).pipe(map(res => res.json()));
    }

    public getCutFile(url: string): Observable<Cut> {
        return this._http.get(url).pipe(map(res => res.json()));
    }

    public postCutFile(fileName: string, videoCutInfo: VideoCutInfo): Observable<boolean> {
        const url = '/api/updateCutFileOf/' + fileName;
        return this._http.post(url, videoCutInfo).pipe(map(res => res.json()));
    }

    public cutVideo(fileName: string, newNameFile: string, container: string): Promise<boolean> {
        const url = '/api/cutVideo/' + fileName + '/' + newNameFile + '/' + container;
        return this._http.get(url).pipe(
            timeout(999999),
            map(res => res.json())).toPromise();
    }

    public mergeVideo(newFileName: string, container: string): Promise<boolean> {
        const url = '/api/mergeVideo/' + newFileName + '/' + container;
        return this._http.post(url, null)
        .pipe(map(res => res.json())).toPromise();
    }

    public createThumbnail(videoName: string, container: string): Promise<boolean> {
        const url = '/api/createThumbnail/' + videoName + '/' + container;
        return this._http.post(url, null)
        .pipe(map(res => res.json())).toPromise();
    }
}
