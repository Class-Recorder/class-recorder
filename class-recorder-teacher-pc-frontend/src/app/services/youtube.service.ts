import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { map } from "rxjs/operators";

@Injectable()
export class YoutubeService {

    constructor(private _http: Http) {
    }

    postVideo(courseId: number, fileName: string, containerFormat: string, videoInfo: any) {
        const url = '/api/uploadVideo/' + courseId + "/" + fileName + "/" + containerFormat;
        return this._http.post(url, videoInfo).pipe(map(res => res.json()));
    }

    getStateUpload() {
        const url = "/api/getStateYoutubeUpload/";
        return this._http.get(url).pipe(map(res => res.text()));
    }

    getYoutubeProgress() {
        const url = "/api/getYoutubeUploadProgress/";
        return this._http.get(url).pipe(map( res => res.text()));
    }

}