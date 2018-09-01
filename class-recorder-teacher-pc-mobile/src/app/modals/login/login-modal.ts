import { Component } from "@angular/core";
import { ViewController, AlertController } from "ionic-angular";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { LoginService } from "../../services/login.service";

@Component({
    selector: 'login-modal',
    templateUrl: 'login-modal.html'
})
export class LoginModal {

    form: FormGroup;

    constructor(public viewCtrl: ViewController,
        private formBuilder: FormBuilder,
        private loginService: LoginService,
        private alertCtrl: AlertController) {
        this.form = this.formBuilder.group({
            email: ['', Validators.compose([Validators.required, Validators.email])],
            password: ['', Validators.required]
        });
    }

    submit() {
        let loginInfo = this.form.value;
        this.loginService.logIn(loginInfo.email, loginInfo.password).subscribe((userLogged) => {
            this.viewCtrl.dismiss(userLogged);
        }, (error) => {
            let alert = this.alertCtrl.create({
                title: 'Error',
                subTitle: 'Incorrect password or email',
                buttons: ['Ok']
            });
            alert.present();
        })
    }

    dismiss() {
        let data = { 'foo': 'bar' };
        this.viewCtrl.dismiss(data);
    }

}
