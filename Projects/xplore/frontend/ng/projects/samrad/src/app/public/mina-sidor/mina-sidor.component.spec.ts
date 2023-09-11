import { HttpClientTestingModule } from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { OAuthModule } from "angular-oauth2-oidc";
import { XpTranslocoTestModule } from "../../../../../lib/translate/translocoTest.module.translate";

import { MinaSidorComponent } from "./mina-sidor.component";

describe("MinaSidorComponent", () => {
  let component: MinaSidorComponent;
  let fixture: ComponentFixture<MinaSidorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        OAuthModule.forRoot(),
        XpTranslocoTestModule,
      ],
      declarations: [
        MinaSidorComponent,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MinaSidorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
