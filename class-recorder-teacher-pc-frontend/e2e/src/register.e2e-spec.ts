import { AppNavigator } from './app.po';
import { LoginPage } from './pages/login-page';
import { MyCourses } from './pages/mycourses-page';
import { RegisterPage } from './pages/register-page';
import { browser } from 'protractor';
import { UserRegistered } from './interfaces/user-registered';

describe('Register user', () => {
    let page: AppNavigator;
    let loginPage: LoginPage;
    let myCourses: MyCourses;
    let registerPage: RegisterPage;
    let userRegistered: UserRegistered;

    beforeAll(() => {
        page = new AppNavigator();
        loginPage = new LoginPage();
        myCourses = new MyCourses();
        registerPage = new RegisterPage();
        userRegistered = {
            username: `username_${Date.now()}`,
            password: '1234',
            email: `email_${Date.now()}@email.com`,
            fullName: `fullname_${Date.now()}`
        };
    });

    it('should open login page and go to register', async () => {
        page.navigateTo('login');
        expect(page.getH1()).toEqual('Welcome to Class Recorder');
        registerPage.clickRegisterButton();
        expect(page.getH1()).toEqual('Register');
        expect(await page.getUrl()).toContain('/register');
    });

    it('should register properly', () => {
        registerPage.register(userRegistered);
        expect(page.getH1()).toEqual('Welcome to Class Recorder');
    });


    it('should login properly', () => {
        loginPage.login(userRegistered.email, userRegistered.password);
        expect(page.getH1()).toEqual('My Classes');
    });

    it('should show all teacher courses', () => {
        expect(myCourses.getCoursesComponents().count()).toEqual(0);
    });

    afterAll(() => {
        browser.restart();
    });
});
