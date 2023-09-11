import { TestBed, async, ComponentFixture } from "@angular/core/testing";
import { By } from "@angular/platform-browser";
import { LoginService } from "../../../../lib/oidc/login.service";
import { NotAuthorizedComponent } from "./not-authorized.component";

describe("NotAuthorizedComponent", () => {
  let fixture: ComponentFixture<NotAuthorizedComponent>;
  let component: NotAuthorizedComponent;
  let element: HTMLElement;
  let mockLoginService;

  beforeEach(async(() => {
    mockLoginService = {
      logout: jest.fn(),
    };

    TestBed.configureTestingModule({
      declarations: [
        NotAuthorizedComponent
      ],
      providers: [
        { provide: LoginService, useValue: mockLoginService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(NotAuthorizedComponent);
    component = fixture.componentInstance;
    element = fixture.nativeElement;
  }));

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska ha rubrik h2", () => {
    const headerElement = element.querySelector("h2");
    expect(headerElement).toBeTruthy();
  });

  it("Ska ha knapp med texten Logga ut", () => {
    const button = element.querySelector("button");

    expect(button.textContent).toContain("Logga ut");
  });

  it("Ska anropa LoginService.logout() vid klick på knapp för att logga ut", () => {
    fixture.debugElement.query(By.css("button")).triggerEventHandler("click", {});

    expect(mockLoginService.logout).toHaveBeenCalledWith();
  });
});
