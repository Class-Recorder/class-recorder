import { Component } from '@angular/core';
import { TeacherService } from '../../services/teacher.service';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';
import { Teacher } from '../../classes/user/Teacher';
import { TeacherDataBindingService } from '../../services/bind-services/teacher-data-binding.service';

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
  currentTheme = 'theme-dark';
  
  constructor(
    private _teacherService: TeacherService,
    private _teacherDataBinding: TeacherDataBindingService) {}

  ngOnInit(){
    this._teacherDataBinding.changeEmitted$.subscribe((teacherInfo: Teacher) => {
      this.teacher = teacherInfo;
      console.log(this.teacher);
    })
  }

  onChangeTheme(){
    console.log(this.currentTheme);
  }

}
