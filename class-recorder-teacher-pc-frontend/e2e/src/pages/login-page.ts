import { browser, by, element, WebElementPromise } from 'protractor';

export class LoginPage {

    login() {
        let usernameInput: WebElementPromise = element(by.name('email')).getWebElement();
        usernameInput.clear();
        usernameInput.sendKeys('juan@juan.com');

        let passwordInput: WebElementPromise = element(by.name('password')).getWebElement();
        passwordInput.clear();
        passwordInput.sendKeys('1234');

        browser.sleep(1000);
        element(by.id('login-button')).click();
        browser.sleep(2000);
    }

}
