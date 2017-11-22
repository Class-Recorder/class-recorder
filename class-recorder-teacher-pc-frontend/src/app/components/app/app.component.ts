import { Component } from '@angular/core';
import { TeacherService } from '../../services/teacher.service';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';
import { Teacher } from '../../classes/user/Teacher';
import { GlobalInfoService } from '../../services/bind-services/global-info-service';
import { OverlayContainer } from '@angular/cdk/overlay';
import { GenericDataBindingService } from '../../services/bind-services/generic-data-binding.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

    teacher: Teacher;
    themes = [
        {value: 'theme-dark', viewValue: 'Dark'},
        {value: 'indigo-pink', viewValue: 'indigo-pink'}
    ];
    currentTheme = 'indigo-pink';
  
    constructor(
        private _teacherService: TeacherService,
        private _genericDataBindingService: GenericDataBindingService,
        private _globalInfoService: GlobalInfoService,
        private _overlayContainer: OverlayContainer) {}

    ngOnInit(){
        this._genericDataBindingService.changeEmitted('teacher-data').subscribe((teacherInfo: Teacher) => {
            this.teacher = teacherInfo;
            this._globalInfoService.loggedTeacher = teacherInfo;
        })
        this.onChangeTheme();
    }

    onChangeTheme(){
        switch(this.currentTheme){
            case 'theme-dark': document.getElementById('html').style.background = '#818181';
            break;
            case 'indigo-pink': document.getElementById('html').style.background = '#c2d6d6';
            break;
        }
        this._overlayContainer.getContainerElement().classList.add(this.currentTheme);
    }

}
