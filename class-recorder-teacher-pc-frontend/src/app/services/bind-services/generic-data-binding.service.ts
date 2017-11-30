import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

export interface CutVideoInfo {
    newNameFile: string;
    containerFormat: string;
}

export const keyServices = [
    'teacher-data',
    'new-file-cutted-video',
    'file-to-cut',
    'console-output-wsocket'
];

@Injectable()
export class GenericDataBindingService {

    private changeSources: Map<String, BehaviorSubject<any>> = new Map();

    constructor() {
        for (const key of keyServices){
            this.changeSources.set(key, new BehaviorSubject<any>(0));
        }
    }

    public emitChange(key: string, change: any): void {
        this.changeSources.get(key).next(change);
        this.changeSources.get(key).complete();
    }

    public changeEmitted(key: string): BehaviorSubject<any> {
        return this.changeSources.get(key);
    }

}
