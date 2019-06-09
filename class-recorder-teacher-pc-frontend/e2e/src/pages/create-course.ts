import { browser, by, element, WebElementPromise } from "protractor";
import { Course } from "../interfaces/course";

export class CoursePage {

    clickAddCourseButton(): void {
        element(by.id('add-course-button')).click();
        browser.sleep(1000);
    }

    createCourse(course: Course) {
        const courseInput: WebElementPromise = element(by.id('formly_1_input_name_0')).getWebElement();
        courseInput.clear();
        courseInput.sendKeys(course.courseName);

        const descriptionInput: WebElementPromise = element(by.id('formly_1_input_description_1')).getWebElement();
        descriptionInput.clear();
        descriptionInput.sendKeys(course.description);

        browser.sleep(1000);
        element(by.id('submit-button')).click();
        browser.sleep(2000);
    }

}