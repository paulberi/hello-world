/* tslint:disable:no-unused-variable */
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { UtcheckningWrapperComponent } from './utcheckning-wrapper.component';


describe('UtcheckningWrapperComponent', () => {
  let component: UtcheckningWrapperComponent;
  let fixture: ComponentFixture<UtcheckningWrapperComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ UtcheckningWrapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UtcheckningWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
