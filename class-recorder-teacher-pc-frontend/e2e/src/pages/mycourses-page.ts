import { browser, by, element, WebElementPromise, ElementArrayFinder, ElementFinder } from 'protractor';
import { getRandomInt } from '../utils';

export class MyCourses {

    getCoursesComponents(): ElementArrayFinder {
        return element.all(by.tagName('app-course-card'));
    }

    async selectRandomCourse(): Promise<void> {
        const allCourses: ElementArrayFinder = this.getCoursesComponents();
        const numCourses: number = await allCourses.count();
        const randomCourseIndex: number = getRandomInt(0, numCourses);
        const elemCourse: ElementFinder = allCourses.get(randomCourseIndex);
        elemCourse.click();
        browser.sleep(2000);
    }

}
