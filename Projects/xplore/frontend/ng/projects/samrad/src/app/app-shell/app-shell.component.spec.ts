import { ComponentFixture, TestBed } from "@angular/core/testing";
import { XpTranslocoTestModule } from "../../../../lib/translate/translocoTest.module.translate";
import { RouterTestingModule } from "@angular/router/testing";
import { AppShellComponent } from "./app-shell.component";
import { XpLayoutModule } from "../../../../lib/ui/layout/layout.module";
import { OAuthModule } from "angular-oauth2-oidc";

describe("AppShellComponent", () => {
  let component: AppShellComponent;
  let fixture: ComponentFixture<AppShellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppShellComponent],
      imports: [
        XpTranslocoTestModule,
        RouterTestingModule.withRoutes([]),
        OAuthModule.forRoot(),
        XpLayoutModule,
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppShellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
