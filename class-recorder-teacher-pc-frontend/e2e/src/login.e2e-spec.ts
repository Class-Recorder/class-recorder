import { AppNavigator } from './app.po';
import { LoginPage } from './pages/login-page';
import { MyCourses } from './pages/mycourses-page';
import { browser } from 'protractor';

describe('Application Login', () => {
  let page: AppNavigator;
  let loginPage: LoginPage;
  let myCourses: MyCourses;

  beforeEach(() => {
    page = new AppNavigator();
    loginPage = new LoginPage();
    myCourses = new MyCourses();
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

  afterAll(() => {
    browser.restart();
  });
});
