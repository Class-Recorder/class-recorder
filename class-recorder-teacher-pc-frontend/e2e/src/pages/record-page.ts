import { browser, by, element, WebElementPromise, ElementFinder, ElementArrayFinder } from 'protractor';
import { Video } from '../interfaces/video';

export class RecordPage {

    clickRecordButton() {
        element(by.id('record-button')).click();
        browser.sleep(1000);
    }

    formVideo(videoInfo: Video) {
        const videoNameInput: WebElementPromise = element(by.name('videoName')).getWebElement();
        videoNameInput.clear();
        videoNameInput.sendKeys(videoInfo.videoName);

        const frameInput: WebElementPromise = element(by.name('frameRate')).getWebElement();
        frameInput.clear();
        frameInput.sendKeys(videoInfo.frameRate);

        element(by.name('ffmpegContainerFormat')).click();
        browser.sleep(1000);
        let options: ElementArrayFinder = element.all(by.tagName('mat-option'));
        options.get(0).click();

        browser.sleep(1000);
        element(by.id('start-button')).click();
        browser.sleep(2000);
    }

}
