import { browser, by, element, ElementArrayFinder } from 'protractor';

export class AppNavigator {

  navigateTo(route: string) {
    return browser.get('/' + route);
  }

  getH1() {
    return element(by.tagName('h1')).getText();
  }

  getUrl() {
    return browser.getCurrentUrl();
  }

  getAllElementsWithClass(cssClass: string): ElementArrayFinder {
    return element.all(by.css(cssClass));
  }
}
