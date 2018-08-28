import { Component, OnInit } from '@angular/core';
import { TeacherService } from '../../services/teacher.service';
import { FormGroup } from '@angular/forms';
import { FormlyFormOptions, FormlyFieldConfig } from '@ngx-formly/core';
import RegisterFields from '../../ngx-formly/RegisterFields';
import { Router } from '@angular/router';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    form = new FormGroup({});
    model: any = {};
    options: FormlyFormOptions = {};

    fields: FormlyFieldConfig[] = RegisterFields;

    constructor(private teacherService: TeacherService,
        private router: Router) { }

    ngOnInit(): void { }

    submit() {
        if (this.form.valid) {
            this.teacherService.registerTeacher(this.model).subscribe((data) => {
                this.router.navigate(['']);
            });
        }
    }
}
