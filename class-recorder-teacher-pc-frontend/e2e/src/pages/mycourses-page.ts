import { browser, by, element, WebElementPromise, ElementArrayFinder } from 'protractor';

export class MyCourses {

    getCoursesComponents(): ElementArrayFinder {
        return element.all(by.tagName('app-course-card'));
    }

}
