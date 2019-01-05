import { Component } from "@angular/core";
import { SlidesService } from "../../../app/services/slides.service";

@Component({
    selector: 'slides-component',
    templateUrl: 'slides-component.html'
})
export class SlidesComponent {
    
    constructor(private slidesService: SlidesService) {

    }

    leftSlide(): void {
        this.slidesService.leftSlide().subscribe((result) => {
            console.log(result);
        });
    }

    rightSlide(): void {
        this.slidesService.rightSlide().subscribe((result) => {
            console.log(result);
        });
    }
}