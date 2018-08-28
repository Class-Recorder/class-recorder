import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';
import CourseFields from '../../ngx-formly/CourseFields';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../../services/course.service';

@Component({
    selector: 'app-add-course-component',
    templateUrl: './add-course.component.html',
    styleUrls: ['./add-course.component.css']
})
export class AddCourseComponent implements OnInit {

    teacherId: number;

    form = new FormGroup({});
    model: any = {};
    options: FormlyFormOptions = {};

    fields: FormlyFieldConfig[] = CourseFields;

    constructor(private activatedRoute: ActivatedRoute,
        private courseService: CourseService,
        private router: Router) { }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe((params) => {
            this.teacherId = params['teacherId'];
        });
    }

    submit() {
        if (this.form.valid) {
            this.courseService.postCourseByTeacherId(this.teacherId, this.model).subscribe((data) => {
                this.router.navigate(['courselist', this.teacherId]);
            });
        }
    }
}
