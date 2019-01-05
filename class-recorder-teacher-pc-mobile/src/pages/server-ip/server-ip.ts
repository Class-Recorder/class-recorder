import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ServerConnectionService } from '../../app/services/server-connection.service';
import { AndroidPermissions } from '@ionic-native/android-permissions';
import { Platform } from 'ionic-angular'; 

@Component({
    selector: 'page-about',
    templateUrl: 'server-ip.html'
})
export class ServerIpPage {

    form : FormGroup;
    ipPattern = '^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$';
    baseUrl: string;

    constructor(private formBuilder: FormBuilder,
        private serverConnectionService: ServerConnectionService,
        private androidPermissions: AndroidPermissions,
        private platform: Platform) {
        
            this.form = this.formBuilder.group({
                ip: ['', Validators.compose([Validators.required, Validators.pattern(this.ipPattern)])],
                port: ['', Validators.required],
            });
            this.checkPermissions().then(() => {});
    }

    async checkPermissions(): Promise<any> {
       if(this.platform.is('android') && this.platform.is('cordova')) {
            await this.platform.ready;
            return this.androidPermissions.requestPermissions(
                [this.androidPermissions.PERMISSION.RECORD_AUDIO, 
                this.androidPermissions.PERMISSION.READ_EXTERNAL_STORAGE, 
                this.androidPermissions.PERMISSION.WRITE_EXTERNAL_STORAGE]
            );
       }
       return;
    }

    submit() {
        const data = this.form.value;
        this.serverConnectionService.connect(data.ip, data.port).subscribe((isConnected) => {
            this.ngOnInit();
        })
    }

    ngOnInit() {
        this.baseUrl = this.serverConnectionService.getBaseUrl();
    }

}


