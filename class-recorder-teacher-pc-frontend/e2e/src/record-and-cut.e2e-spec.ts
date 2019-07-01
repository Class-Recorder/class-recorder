import { AppNavigator } from './app.po';
import { LoginPage } from './pages/login-page';
import { MyCourses } from './pages/mycourses-page';
import { browser, ElementArrayFinder} from 'protractor';
import { RecordPage } from './pages/record-page';
import { Video } from './interfaces/video';

describe('Record test', () => {
    let page: AppNavigator;
    let loginPage: LoginPage;
    let myCourses: MyCourses;
    let recordPage: RecordPage;
    let videoInfo: Video;
    let dateString = Date.now();

    beforeAll(() => {
        jasmine.DEFAULT_TIMEOUT_INTERVAL = 100000;
    });

    beforeEach(() => {
        page = new AppNavigator();
        loginPage = new LoginPage();
        myCourses = new MyCourses();
        recordPage = new RecordPage();
        videoInfo = {
            videoName: `video_${dateString}`,
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
        recordPage.formVideo(videoInfo);
        browser.sleep(5000);
        browser.executeScript('return window.AngularRecord.pauseRecord();');
        browser.sleep(5000);
        browser.executeScript('return window.AngularRecord.continueRecord();');
        browser.sleep(5000);
        browser.executeScript('return window.AngularRecord.pauseRecord();');
        browser.sleep(5000);
        browser.executeScript('return window.AngularRecord.continueRecord();');
        browser.sleep(5000);
        browser.executeScript('return window.AngularRecord.stopRecord();');
        browser.sleep(5000);
    });

    it('should be a video recorded', async () => {
        await myCourses.selectRandomCourse();
        browser.sleep(5000);
        let allVideos: ElementArrayFinder = page.getAllElementsWithClass('.video-title');
        let hasVideo = false;
        for (let i = 0; i < await allVideos.count(); i++) {
            if ((await allVideos.get(i).getText()).includes(videoInfo.videoName)) {
                hasVideo = true;
                break;
            }
        }
        expect(hasVideo).toBe(true);
    });

    afterAll(() => {
        browser.restart();
    });
});
