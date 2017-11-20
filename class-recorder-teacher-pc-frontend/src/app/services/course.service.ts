import { Injectable } from "@angular/core";
import { Http } from '@angular/http';
import { Observable } from "rxjs/Observable";
import { Course } from "../classes/Course";

@Injectable()
export class CourseService{

    constructor(private _http: Http){}

    getCoursesByTeacherId(id: number): Observable<Course[]> {
        let url = "/api/getCoursesByTeacherId/" + id;
        return this._http.get(url).map(res => res.json());
    } 
}