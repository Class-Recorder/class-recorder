import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { User } from '../../classes/user/User';
import { Teacher } from '../../classes/user/Teacher';
import { LoginForm } from '../../form-classes/login-form';
import { TeacherService } from '../../services/teacher.service';
import { FormBuilder, Validators } from '@angular/forms';
import { FormGroup } from '@angular/forms/src/model';
import { Router } from '@angular/router';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    user: Teacher;
    loginFormInfo: LoginForm;
    loginFormValidator: FormGroup;
    isValidFormSubmitted = null;

    // Alerts
    unauthorized: boolean;
    notTeacher: boolean;


    constructor(
        private _loginService: LoginService,
        private _teacherService: TeacherService,
        private _genericDataBinding: GenericDataBindingService,
        private _formBuilder: FormBuilder,
        private _router: Router
    ) {

        this.loginFormInfo = new LoginForm();

        this.loginFormValidator = this._formBuilder.group({
            email : ['', Validators.compose([Validators.email, Validators.required])],
            password : ['', Validators.required]
        });
    }

    ngOnInit() {
    }

    onKey(event: any) {
        this.unauthorized = false;
        this.notTeacher = false;
    }

    onSubmit() {
        const email: string = this.loginFormInfo.email;
        const password: string = this.loginFormInfo.password;
        this._loginService.logIn(email, password).subscribe(userInfo => {
            const u: User = userInfo;
            this._teacherService.getTeacherInfo(u.id).subscribe((teacherInfo) => {
                this._genericDataBinding.emitChangeSubject('login-succesful', teacherInfo.id);
            }, (error) => {
                this.notTeacher = true;
            });
        }, (error) => {
            if (error.status === 401){
                this.unauthorized = true;
            }
        });
    }

}
