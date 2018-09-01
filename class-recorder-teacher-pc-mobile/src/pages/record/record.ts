import { Component } from '@angular/core';
import { NavController, AlertController, ModalController } from 'ionic-angular';
import { ServerConnectionService } from '../../app/services/server-connection.service';
import { LoginModal } from '../../app/modals/login/login-modal';
import { LoginService } from '../../app/services/login.service';



@Component({
  selector: 'page-home',
  templateUrl: 'record.html'
})
export class RecordPage {

    connectedToServer: boolean = false;
    userLogged: boolean = false;

    constructor(public navCtrl: NavController,
        private serverConnectionService: ServerConnectionService,
        private loginService: LoginService,
        private alertCtrl: AlertController,
        public modalCtrl: ModalController) {
    }

    ionViewWillEnter() {
        this.serverConnectionService.checkConnection().subscribe(() => {
            this.connectedToServer = true;
            if(this.loginService.user === undefined) {
                this.loginService.reqIsLogged().then(() => {
                    this.userLogged = true;
                }).catch(() => {
                    let profileModal = this.modalCtrl.create(LoginModal);
                    profileModal.onDidDismiss(data => {
                        if(data !== undefined) {
                            this.userLogged = true;
                        }
                    });
                    profileModal.present();       
                });
            } else {
                this.userLogged = true;
            }
        }, (error) => {
            this.notConnectedAlert();
        });
    }

    notConnectedAlert() {
        let alert = this.alertCtrl.create({
            title: 'Warning',
            subTitle: 'You are not connected to your pc',
            buttons: ['Ok']
        });
        alert.present();
        this.navCtrl.parent.select(0);
    }

}
