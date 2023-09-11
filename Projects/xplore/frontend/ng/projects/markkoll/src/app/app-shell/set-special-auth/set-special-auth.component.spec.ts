import { ComponentFixture, TestBed } from "@angular/core/testing";
import { RouterTestingModule } from "@angular/router/testing";

import { MkSetSpecialAuthComponent } from "./set-special-auth.component";

describe("MkSetSpecialAuthComponent", () => {
  let component: MkSetSpecialAuthComponent;
  let fixture: ComponentFixture<MkSetSpecialAuthComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MkSetSpecialAuthComponent ],
      imports: [RouterTestingModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MkSetSpecialAuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
