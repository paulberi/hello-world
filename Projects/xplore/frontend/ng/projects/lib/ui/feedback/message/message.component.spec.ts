import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { MatIconModule } from "@angular/material/icon";
import { By } from "@angular/platform-browser";
import { XpTranslocoTestModule } from "../../../translate/translocoTest.module.translate";
import { XpMessageComponent, XpMessageSeverity } from "./message.component";

describe("XpMessageComponent", () => {
  let component: XpMessageComponent;
  let fixture: ComponentFixture<XpMessageComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [XpMessageComponent],
      imports: [
        MatIconModule,
        XpTranslocoTestModule
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(XpMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it("Ska visa komponent", () => {
    const messageElement = fixture.debugElement.query(By.css(".xp-message"));
    const containerElement = fixture.debugElement.query(By.css(".xp-message-container"));

    expect(component).toBeTruthy();
    expect(messageElement).toBeTruthy();
    expect(containerElement).toBeTruthy();
  });

  it("Ska visa text för meddelande", () => {
    const mockText = "Mocktext";

    component.text = mockText;
    fixture.detectChanges();

    const textContent = fixture.debugElement.query(By.css("span")).nativeElement.textContent;
    expect(textContent).toEqual(mockText);
  });

  it("Ska kunna slå av att stänga meddelande", () => {
    component.isClosable = false;    
    fixture.detectChanges();

    const closeButton = fixture.debugElement.query(By.css(".close"));
    expect(closeButton).toBeFalsy();
  });


  it("Ska emitta event vid klick på 'Stäng'", () => {
    const onClose = jest.spyOn(component.onClose, "emit");
    const mockSeverity = XpMessageSeverity.Error;

    component.severity = mockSeverity;
    fixture.detectChanges();

    const closeButton = fixture.debugElement.query(By.css("button")).nativeElement;
    closeButton.click();

    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it("Ska inte visa en action-knapp som standard", () => {
    const actionButton = fixture.debugElement.query(By.css(".action"));   
    
    expect(component.isActionable).toBe(false);
    expect(actionButton).toBeFalsy();
  });

  it("Ska kunna slå på action-knapp", () => {
    component.isActionable = true;
    fixture.detectChanges();

    const actionButton = fixture.debugElement.query(By.css(".action")).nativeElement;
    expect(actionButton).toBeTruthy();
  });

  it("Ska visa text på action-knapp", () => {
    component.isActionable = true;
    component.actionLabel = "Let's do this";
    fixture.detectChanges();

    const actionButton = fixture.debugElement.query(By.css(".action")).nativeElement.textContent;
    expect(actionButton).toEqual(component.actionLabel);
  });


  it("Ska emitta event vid klick på action-knapp", () => {
    const onAction = jest.spyOn(component.onAction, "emit");            
    component.isActionable = true;
    fixture.detectChanges();

    const actionButton = fixture.debugElement.query(By.css(".action")).nativeElement;
    actionButton.click();

    expect(onAction).toHaveBeenCalledTimes(1);
  });


  it("Ska visa meddelande med nivå 'Success'", () => {
    const successSeverity = XpMessageSeverity.Success;
    const successIconClass = "done";

    component.severity = successSeverity;
    fixture.detectChanges();

    const messageElement = fixture.debugElement.query(By.css(".xp-message-success"));
    const iconClass = fixture.debugElement.query(By.css(".mat-icon")).nativeElement.textContent;
    expect(messageElement).toBeTruthy();
    expect(iconClass).toEqual(successIconClass);
  });

  it("Ska visa meddelande med nivå 'Information'", () => {
    const informationSeverity = XpMessageSeverity.Information;
    const informationIconClass = "info_outline";

    component.severity = informationSeverity;
    fixture.detectChanges();

    const messageElement = fixture.debugElement.query(By.css(".xp-message-information"));
    const iconClass = fixture.debugElement.query(By.css(".mat-icon")).nativeElement.textContent;
    expect(messageElement).toBeTruthy();
    expect(iconClass).toEqual(informationIconClass);
  });

  it("Ska visa meddelande med nivå 'Warning'", () => {
    const warningSeverity = XpMessageSeverity.Warning;
    const warningIconClass = "warning";

    component.severity = warningSeverity;
    fixture.detectChanges();

    const messageElement = fixture.debugElement.query(By.css(".xp-message-warning"));
    const iconClass = fixture.debugElement.query(By.css(".mat-icon")).nativeElement.textContent;
    expect(messageElement).toBeTruthy();
    expect(iconClass).toEqual(warningIconClass);
  });

  it("Ska visa meddelande med nivå 'Error'", () => {
    const errorSeverity = XpMessageSeverity.Error;
    const errorIconClass = "error_outline";

    component.severity = errorSeverity;
    fixture.detectChanges();

    const messageElement = fixture.debugElement.query(By.css(".xp-message-error"));
    const iconClass = fixture.debugElement.query(By.css(".mat-icon")).nativeElement.textContent;
    expect(messageElement).toBeTruthy();
    expect(iconClass).toEqual(errorIconClass);
  });
});
