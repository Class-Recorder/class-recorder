import { Injectable } from '@angular/core';
import { Teacher } from '../../classes/user/Teacher';

@Injectable()
export class GlobalInfoService {
    public loggedTeacher: Teacher;

    constructor() {}
}

