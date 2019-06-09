import { AppNavigator } from './app.po';
import { LoginPage } from './pages/login-page';
import { MyCourses } from './pages/mycourses-page';

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
    loginPage.login();
    expect(page.getH1()).toEqual('My Classes');
  });

  it('should show all teacher courses', () => {
    console.log(myCourses.getCoursesComponents().count());
    expect(myCourses.getCoursesComponents().count()).toBeGreaterThan(0);
  });
});
