import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ViewController } from 'ionic-angular';

@Component({
    selector: 'add-video',
    templateUrl: 'add-video.html'
})
export class AddVideoModal implements OnInit {

    form: FormGroup;
    frameRateRegex = '^([1-2][0-9]|[3-5][0-9]|60)$';

    constructor(private formBuilder: FormBuilder,
        private viewController: ViewController) {
        this.form = this.formBuilder.group({
            'videoName': ['', Validators.required],
            'frameRate': ['', Validators.compose([Validators.required, Validators.pattern(this.frameRateRegex)])],
            'ffmpegContainerFormat': ['', Validators.required]
        })
     }

    ngOnInit(): void { }

    submit() {
        this.viewController.dismiss(this.form.value);
    }
}
