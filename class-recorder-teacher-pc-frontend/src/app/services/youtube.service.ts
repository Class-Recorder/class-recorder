import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { map } from 'rxjs/operators';

@Injectable()
export class YoutubeService {

    constructor(private _http: Http) {
    }

    postVideo(courseId: number, fileName: string, containerFormat: string, videoInfo: any) {
        const url = '/api/uploadVideo/' + courseId + '/' + fileName + '/' + containerFormat;
        return this._http.post(url, videoInfo).pipe(map(res => res.json()));
    }

    updateVideo(videoId: number, videoInfo: any) {
        const url = '/api/updateVideoInfo/' + videoId;
        return this._http.put(url, videoInfo).pipe(map(res => res.json()));
    }

    deleteVideo(videoId: number) {
        const url = '/api/deleteVideo/' + videoId;
        return this._http.delete(url).pipe(map(res => res.json()));
    }

    getStateUpload() {
        const url = '/api/getStateYoutubeUpload/';
        return this._http.get(url).pipe(map(res => res.text()));
    }

    getYoutubeProgress() {
        const url = '/api/getYoutubeUploadProgress/';
        return this._http.get(url).pipe(map( res => res.text()));
    }

    getYoutubeVideoById(videoId: number) {
        const url = '/api/getVideoInfo/' + videoId;
        return this._http.get(url).pipe(map(res => res.json()));
    }

    getYoutubeVideos(courseId: number, page: number) {
        const url = '/api/getUploadedVideos/' + courseId + '/?page=' + page;
        return this._http.get(url).pipe(map(res => res.json()));
    }

}
