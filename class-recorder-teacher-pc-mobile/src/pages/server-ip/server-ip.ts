import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ServerConnectionService } from '../../app/services/server-connection.service';

@Component({
    selector: 'page-about',
    templateUrl: 'server-ip.html'
})
export class ServerIpPage {

    form : FormGroup;
    ipPattern = '^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$';
    baseUrl: string;

    constructor(private formBuilder: FormBuilder,
        private serverConnectionService: ServerConnectionService) {
        this.form = this.formBuilder.group({
            ip: ['', Validators.compose([Validators.required, Validators.pattern(this.ipPattern)])],
            port: ['', Validators.required],
        });
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


