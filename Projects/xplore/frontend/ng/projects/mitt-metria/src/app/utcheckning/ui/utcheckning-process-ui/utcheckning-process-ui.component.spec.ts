/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { UtcheckningProcessUiComponent } from './utcheckning-process-ui.component';
import { TranslocoTestingModule } from '@ngneat/transloco';

describe('UtcheckningProcessUiComponent', () => {
  let component: UtcheckningProcessUiComponent;
  let fixture: ComponentFixture<UtcheckningProcessUiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UtcheckningProcessUiComponent ],
      imports: [TranslocoTestingModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UtcheckningProcessUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
