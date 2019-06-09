import { AppNavigator } from './app.po';
import { LoginPage } from './pages/login-page';
import { MyCourses } from './pages/mycourses-page';
import { RegisterPage } from './pages/register-page';
import { browser, ElementArrayFinder, ElementFinder } from 'protractor';
import { UserRegistered } from './interfaces/user-registered';
import { CoursePage } from './pages/create-course';
import { Course } from './interfaces/course';

describe('Create Course', () => {
    let page: AppNavigator;
    let loginPage: LoginPage;
    let myCourses: MyCourses;
    let createCoursePage: CoursePage;
    let course: Course;

    beforeAll(() => {
        page = new AppNavigator();
        loginPage = new LoginPage();
        myCourses = new MyCourses();
        createCoursePage = new CoursePage;
        course = {
            courseName: `course_${Date.now()}`,
            description: `test course`
        };
    });


    it('should login properly', () => {
        page.navigateTo('login');
        loginPage.login('juan@juan.com', '1234');
        expect(page.getH1()).toEqual('My Classes');
    });

    it('should be able to create a course', async () => {
        createCoursePage.clickAddCourseButton();
        createCoursePage.createCourse(course);
        let allCourses: ElementArrayFinder = myCourses.getCoursesComponents();
        let elementWithNewCourse: string;
        await allCourses.each(async (courseElem: ElementFinder) => {
            let courseHtml = await courseElem.getText();
            if (courseHtml.includes(course.courseName)) {
                elementWithNewCourse = courseHtml;
            }
        });
        console.log(elementWithNewCourse);
        expect(elementWithNewCourse).toBeDefined();
    });

    /*
    it('should be able to create a course', () => {
        expect(myCourses.getCoursesComponents().count()).toEqual(0);
    });
    */

    afterAll(() => {
        browser.restart();
    });
});
