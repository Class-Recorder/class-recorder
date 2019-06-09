import { AppNavigator } from './app.po';
import { LoginPage } from './pages/login-page';
import { MyCourses } from './pages/mycourses-page';
import { browser, ElementArrayFinder, ElementFinder } from 'protractor';
import { getRandomInt } from './utils';
import { RecordPage } from './pages/record-page';
import { Video } from './interfaces/video';

describe('Record test', () => {
    let page: AppNavigator;
    let loginPage: LoginPage;
    let myCourses: MyCourses;
    let recordPage: RecordPage;
    let videoInfo: Video;

    beforeEach(() => {
        page = new AppNavigator();
        loginPage = new LoginPage();
        myCourses = new MyCourses();
        recordPage = new RecordPage();
        videoInfo = {
            videoName: `video_${Date.now()}`,
            frameRate: 60
        };
    });

    it('should open login page', () => {
        page.navigateTo('login');
        expect(page.getH1()).toEqual('Welcome to Class Recorder');
    });

    it('should login properly', () => {
        loginPage.login('juan@juan.com', '1234');
        expect(page.getH1()).toEqual('My Classes');
    });

    it('should show all teacher courses', () => {
        expect(myCourses.getCoursesComponents().count()).toBeGreaterThan(0);
    });

    it('should select a random course', async () => {
        await myCourses.selectRandomCourse();
    });

    it('should start recording', () => {
        recordPage.clickRecordButton();
        expect(page.getH1()).toEqual('Record new video');
    });

    afterAll(() => {
        browser.restart();
    });
});
