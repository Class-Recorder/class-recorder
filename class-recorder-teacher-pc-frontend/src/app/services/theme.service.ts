import { Injectable } from '@angular/core';

@Injectable()
export class ThemeService {
    private theme: string;

    constructor() {}

    getTheme() {
        return this.theme;
    }

    setTheme(theme: string) {
        this.theme = theme;
    }
}
