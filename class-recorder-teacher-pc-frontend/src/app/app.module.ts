import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MatToolbarModule, MatIconModule, MatButtonModule,
  MatMenuModule, MatGridListModule, MatCardModule, MatFormFieldModule, MatInputModule,
  MatSelectModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpModule } from '@angular/http';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';

import { AppComponent } from './components/app/app.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { routes } from './app.routes';
import { LoginComponent } from './components/login/login.component';

import {LoginService} from './services/login.service'
import {TeacherService} from './services/teacher.service'
import {TeacherDataBindingService} from './services/bind-services/teacher-data-binding.service'

@NgModule({
  declarations: [
    AppComponent,
    LoginRegisterComponent,
    LoginComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    BrowserAnimationsModule,
    NoopAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatGridListModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    FlexLayoutModule
  ],
  providers: [LoginService, TeacherService, TeacherDataBindingService],
  bootstrap: [AppComponent]
})
export class AppModule { }
