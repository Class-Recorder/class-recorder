import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Course } from '../classes/Course';

@Injectable()
export class CourseService {

    constructor(private _http: Http) {}

    public getCoursesByTeacherId(id: number): Observable<Course[]> {
        const url = '/api/getCoursesByTeacherId/' + id;
        return this._http.get(url).pipe(map(res => res.json()));
    }

    public getCourseById(id: number): Observable<Course[]> {
        const url = '/api/getCourseById/' + id;
        return this._http.get(url).pipe(map(res => res.json()));
    }

    public postCourseByTeacherId(teacherId: number, data: any): Observable<Course> {
        const url = '/api/postCourse/' + teacherId;
        return this._http.post(url, data).pipe(map(res => res.json()));
    }

    public putCourseById(courseId: number, data: any): Observable<Course> {
        const url = '/api/updateCourse/' + courseId;
        return this._http.put(url, data).pipe(map(res => res.json()));
    }

    public deleteCourseById(courseId: number): Observable<boolean> {
        const url = '/api/deleteCourse/' + courseId;
        return this._http.delete(url).pipe(map(res => res.json()));
    }
}
