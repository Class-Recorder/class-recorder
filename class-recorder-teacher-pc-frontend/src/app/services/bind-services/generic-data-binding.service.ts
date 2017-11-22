import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

export const keyServices = [
    'teacher-data',
    'new-file-cutted-video',
    'file-to-cut'
]

@Injectable()
export class GenericDataBindingService {

    private changeSources:Map<String, BehaviorSubject<any>> = new Map();

    constructor(){
        for(let key of keyServices){
            this.changeSources.set(key, new BehaviorSubject<any>(0));
        }
    }

    public emitChange(key: string, change: any): void{
        this.changeSources.get(key).next(change);
    }

    public changeEmitted(key: string): Observable<any>{
        return this.changeSources.get(key).asObservable();
    }

}