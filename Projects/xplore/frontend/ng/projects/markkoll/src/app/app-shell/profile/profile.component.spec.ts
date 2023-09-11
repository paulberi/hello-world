import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatRadioModule } from "@angular/material/radio";
import { MatInputModule } from "@angular/material/input";
import { MatIconModule } from "@angular/material/icon";
import { MatFormFieldModule } from "@angular/material/form-field";
import { XpUserService } from "../../../../../lib/user/user.service";
import { ProfileComponent } from "./profile.component";
import { HttpClientModule } from "@angular/common/http";
import { XpTranslocoTestModule } from "../../../../../lib/translate/translocoTest.module.translate";

describe("ProfileComponent", () => {
  let mockXpUserService: any;
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let element: HTMLElement;

  beforeEach(waitForAsync(() => {
    mockXpUserService = {
      getUser: jest.fn(_ => ({
        loggedIn: true,
        claims: {name: "Test Testsson", given_name: "Test", family_name: "Testsson", email: "test@test"},
        roles: ["markkoll_markagare"]
      }))
    };
    TestBed.configureTestingModule({
      declarations: [
        ProfileComponent
      ],
      imports: [
        MatIconModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatRadioModule,
        MatInputModule,
        MatButtonModule,
        XpTranslocoTestModule,
        HttpClientModule
      ],
      providers: [
        { provide: XpUserService, useValue: mockXpUserService }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    element = fixture.nativeElement;
    fixture.detectChanges();
  });

  it("Ska kunna skapa komponent", waitForAsync(() => {
    expect(component).toBeTruthy();
  }));
});
