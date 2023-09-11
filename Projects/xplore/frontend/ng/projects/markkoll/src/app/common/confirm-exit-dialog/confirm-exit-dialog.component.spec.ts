import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatDialogRef } from "@angular/material/dialog";
import { By } from "@angular/platform-browser";
import { MockInstance, MockProvider } from "ng-mocks";
import { XpTranslocoTestModule } from "../../../../../lib/translate/translocoTest.module.translate";
import { clickButtonBySelector } from "../../../test/jest-util";

import { ConfirmExitDialogComponent } from "./confirm-exit-dialog.component";

describe("ConfirmExitDialogComponent", () => {
  let component: ConfirmExitDialogComponent;
  let fixture: ComponentFixture<ConfirmExitDialogComponent>;
  let matDialogClose: any;

  beforeEach(async () => {
    matDialogClose = jest.fn();

    await TestBed.configureTestingModule({
      declarations: [ ConfirmExitDialogComponent ],
      imports: [
        XpTranslocoTestModule,
      ],
      providers: [
        MockProvider(MatDialogRef, {
          close: matDialogClose
        })
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmExitDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("ska bekräfta osparade ändringar när man trycker på OK", async () => {
    // When
    await clickButtonBySelector(fixture, "#ok");

    // Then
    expect(matDialogClose).toHaveBeenCalledTimes(1);
    expect(matDialogClose).toHaveBeenCalledWith(true);
  });

  it("ska avbryta när man trycker på avbryt", async () => {
    // When
    await clickButtonBySelector(fixture, "#cancel");

    // Then
    expect(matDialogClose).toHaveBeenCalledTimes(1);
    expect(matDialogClose).toHaveBeenCalledWith(false);
  });
});
