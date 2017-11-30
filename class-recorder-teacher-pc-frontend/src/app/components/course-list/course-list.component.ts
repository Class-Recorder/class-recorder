import { Component, OnInit } from '@angular/core';
import { GlobalInfoService } from '../../services/bind-services/global-info-service';
import { Teacher } from '../../classes/user/Teacher';
import { Course } from '../../classes/Course';
import { CourseService } from '../../services/course.service';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';

@Component({
    selector: 'app-course-list',
    templateUrl: './course-list.component.html',
    styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit {

    teacher: Teacher;
    courses: Course[];

    constructor(
        private _globalInfoService: GlobalInfoService,
        private _courseService: CourseService,
        private _processWebSocket: WebSocketProcessInfo) {}

    ngOnInit() {
        this.teacher = this._globalInfoService.loggedTeacher;
        console.log(this.teacher);
        this._courseService.getCoursesByTeacherId(this.teacher.id).subscribe((coursesInfo) => {
            this.courses = coursesInfo;
            console.log(this.courses);
        });
    }

}
