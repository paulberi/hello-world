import { HttpClientTestingModule } from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { OAuthModule } from "angular-oauth2-oidc";
import { XpTranslocoTestModule } from "../../../../../../lib/translate/translocoTest.module.translate";
import { CartItemsComponent } from "./cart-items.component";
import { XpUserService } from "../../../../../../lib/user/user.service";
import { of } from "rxjs";
import { MockService } from "ng-mocks";
import { AdminAdjustOrderLineGQL } from "../../data-access/cart-items.admin.generated";

describe("CartItemsComponent", () => {
  let fixture: ComponentFixture<CartItemsComponent>;
  let component: CartItemsComponent;

  let mockAdminAdjustOrderLineGQL: any;
  let mockXpUserService: any;

  beforeEach(async () => {
    mockXpUserService = MockService(XpUserService, {
      getUser: jest.fn().mockReturnValue(of(
        {
          loggedIn: true,
          claims: { name: "Test Testsson", given_name: "Test", family_name: "Testsson", email: "test@test" },
          roles: ["metria_saljare"]
        }
      ))
    });

    await TestBed.configureTestingModule({
      declarations: [CartItemsComponent],
      imports: [
        HttpClientTestingModule,
        MatDividerModule,
        MatIconModule,
        MatTableModule,
        XpTranslocoTestModule,
        OAuthModule.forRoot()
      ],
      providers: [
        { provide: AdminAdjustOrderLineGQL, useValue: mockAdminAdjustOrderLineGQL },
        { provide: XpUserService, useValue: mockXpUserService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CartItemsComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
