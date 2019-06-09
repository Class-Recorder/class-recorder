import { browser, by, element, WebElementPromise } from 'protractor';
import { UserRegistered } from '../interfaces/user-registered';

export class RegisterPage {

    clickRegisterButton(): void {
        element(by.id('register-button')).click();
        browser.sleep(1000);
    }

    register(userRegistered: UserRegistered) {
        const userName: WebElementPromise = element(by.id('formly_1_input_userName_0')).getWebElement();
        userName.clear();
        userName.sendKeys(userRegistered.username);

        const passwordInput: WebElementPromise = element(by.id('formly_1_input_password_1')).getWebElement();
        passwordInput.clear();
        passwordInput.sendKeys(userRegistered.password);

        const emailInput: WebElementPromise = element(by.id('formly_1_input_email_2')).getWebElement();
        emailInput.clear();
        emailInput.sendKeys(userRegistered.email);

        const fullNameInput: WebElementPromise = element(by.id('formly_1_input_fullName_3')).getWebElement();
        fullNameInput.clear();
        fullNameInput.sendKeys(userRegistered.fullName);

        browser.sleep(1000);
        element(by.id('register-button')).click();
        browser.sleep(2000);
    }

}
