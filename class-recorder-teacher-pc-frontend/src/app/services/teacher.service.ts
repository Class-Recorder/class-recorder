import { Injectable, OnInit } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { Teacher } from '../classes/user/Teacher';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class TeacherService {

    constructor(private _http:Http){
    }

    public getTeacherInfo(id: number): any{
        const url: string = '/api/teacherInfo/' + id;

        return this._http.get(url).map(res => res.json());
    }
}