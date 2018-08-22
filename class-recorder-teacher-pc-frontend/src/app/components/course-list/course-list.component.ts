import { Component, OnInit } from '@angular/core';
import { GlobalInfoService } from '../../services/bind-services/global-info-service';
import { Teacher } from '../../classes/user/Teacher';
import { Course } from '../../classes/Course';
import { CourseService } from '../../services/course.service';
import { WebSocketProcessInfo } from '../../services/websocket-services/WebSocketProcessInfo';
import { WebSocketYoutubeProgress } from '../../services/websocket-services/WebSocketYoutubeProgress';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-course-list',
    templateUrl: './course-list.component.html',
    styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit {

    teacherId: number;
    courses: Course[];

    constructor(
        private _courseService: CourseService,
        private _activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this._activatedRoute.params.subscribe(params => {
            this.teacherId = params['teacherId'];
            this._courseService.getCoursesByTeacherId(this.teacherId).subscribe((coursesInfo) => {
                this.courses = coursesInfo;
                console.log(this.courses);
            });
        });
    }

}
