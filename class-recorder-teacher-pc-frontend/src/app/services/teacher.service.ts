import { Injectable, OnInit } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { Teacher } from '../classes/user/Teacher';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class TeacherService {

    constructor(private _http: Http) {
    }

    public getTeacherInfo(id: number): any {
        const url: string = '/api/teacherInfo/' + id;
        return this._http.get(url).pipe(map(res => res.json()));
    }

    public registerTeacher(teacher: any) {
        const url = '/api/registerTeacher';
        return this._http.post(url, teacher).pipe(map(res => res.json()));
    }
}
