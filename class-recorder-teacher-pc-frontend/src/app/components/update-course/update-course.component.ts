import { Component, OnInit, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';
import CourseFields from '../../ngx-formly/CourseFields';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../../services/course.service';
import { LoginService } from '../../services/login.service';

@Component({
    selector: 'app-update-course-component',
    templateUrl: './update-course.component.html',
    styleUrls: ['./update-course.component.css']
})
export class UpdateCourseComponent implements OnInit {

    courseId: number;

    form = new FormGroup({});
    model: any = {};
    options: FormlyFormOptions = {};

    fields: FormlyFieldConfig[] = CourseFields;

    constructor(private activatedRoute: ActivatedRoute,
        private courseService: CourseService,
        private router: Router,
        private loginService: LoginService) { }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe((params) => {
            this.courseId = params['courseId'];
            this.courseService.getCourseById(this.courseId).subscribe((course) => {
                this.model = course;
            });
        });
    }

    submit() {
        if (this.form.valid) {
            this.courseService.putCourseById(this.courseId, this.model).subscribe((data) => {
                this.router.navigate(['courselist', this.loginService.user.id]);
            });
        }
    }
}
