import { browser, by, element, WebElementPromise } from 'protractor';

export class LoginPage {

    login(username: string, password: string) {
        const usernameInput: WebElementPromise = element(by.name('email')).getWebElement();
        usernameInput.clear();
        usernameInput.sendKeys(username);

        const passwordInput: WebElementPromise = element(by.name('password')).getWebElement();
        passwordInput.clear();
        passwordInput.sendKeys(password);

        browser.sleep(1000);
        element(by.id('login-button')).click();
        browser.sleep(2000);
    }

}
