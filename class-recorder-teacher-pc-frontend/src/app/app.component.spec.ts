import { TestBed, async } from '@angular/core/testing';
import { AppComponentTest } from './app.component';
describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponentTest
      ],
    }).compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponentTest);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
  it(`should have as title 'class-recorder-teacher-pc-frontend'`, async(() => {
    const fixture = TestBed.createComponent(AppComponentTest);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('class-recorder-teacher-pc-frontend');
  }));
  it('should render title in a h1 tag', async(() => {
    const fixture = TestBed.createComponent(AppComponentTest);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to class-recorder-teacher-pc-frontend!');
  }));
});
