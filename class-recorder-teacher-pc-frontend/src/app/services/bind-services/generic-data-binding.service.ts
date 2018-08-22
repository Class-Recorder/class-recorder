import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';

export interface CutVideoInfo {
    newNameFile: string;
    containerFormat: string;
}

export const keyServicesSubject = [
    'login-succesful',
    'get-recording-state',
    'youtube-upload'
];

export const keyServicesBehaviorSubject = [
    'file-to-cut',
    'new-file-cutted-video'
];


@Injectable()
export class GenericDataBindingService {

    private changeSourcesSubject: Map<String, Subject<any>> = new Map();
    private changeSourcesBehaviorSubject: Map<String, BehaviorSubject<any>> = new Map();

    constructor() {
        for (const key of keyServicesSubject){
            this.changeSourcesSubject.set(key, new Subject<any>());
        }
        for(const key of keyServicesBehaviorSubject) {
            this.changeSourcesBehaviorSubject.set(key, new BehaviorSubject<any>(0));
        }
    }

    public emitChangeBehaviorSubject(key: string, change?: any): void {
        this.changeSourcesBehaviorSubject.get(key).next(change);
        this.changeSourcesBehaviorSubject.get(key).complete();
    }

    public changeEmittedBehaviorSubject(key: string): BehaviorSubject<any> {
        return this.changeSourcesBehaviorSubject.get(key);
    }

    public emitChangeSubject(key: string, change?: any) {
        this.changeSourcesSubject.get(key).next(change);
    }

    public changeEmittedSubject(key: string): Subject<any> {
        return this.changeSourcesSubject.get(key);
    }

}
