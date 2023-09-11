import { OverlayContainer } from "@angular/cdk/overlay";
import { DebugElement, Type } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { By } from "@angular/platform-browser";

export function clickButtonBySelector<T>(fixture: ComponentFixture<T>, selector: string): Promise<void> {
  let button: any;

  try {
    button = queryBySelector(fixture, selector).nativeElement;
  } catch (err) {
    console.log("Cannot find element with selector '" + selector + "'");
  }

  button.click();

  fixture.detectChanges();
  return fixture.whenStable();
}

export function queryBySelector<T>(fixture: ComponentFixture<T>, selector: string): DebugElement {
  return fixture.debugElement.query(By.css(selector));
}

export function queryByDirective<T>(fixture: ComponentFixture<T>, directive: Type<any>): DebugElement {
  return fixture.debugElement.query(By.directive(directive));
}

export function queryAllBySelector<T>(fixture: ComponentFixture<T>, selector: string): DebugElement[] {
  return fixture.debugElement.queryAll(By.css(selector));
}

export function queryComponent<T, D>(fixture: ComponentFixture<T>, type: Type<D>): D {
  return queryByDirective(fixture, type).context as D;
}

// https://stackoverflow.com/questions/52505846/angular-6-unit-testing-mat-select
export function setMatSelectValue<T>(fixture: ComponentFixture<T>, element: HTMLElement, label: string): Promise<void> {
  // click on <mat-select>
  element.click();
  fixture.detectChanges();

  // options will be rendered inside OverlayContainer
  const overlay = TestBed.inject(OverlayContainer).getContainerElement();

  // find an option by text value and click it
  const matOption = Array.from(overlay.querySelectorAll<HTMLElement>(".mat-option span.mat-option-text"))
    .find(opt => opt.textContent.includes(label));
  matOption.click();

  fixture.detectChanges();
  return fixture.whenStable();
}

export function setInputValueBySelector<T>(fixture: ComponentFixture<T>, value: any, selector: string) {
  const inputElement = queryBySelector(fixture, "input").nativeElement;
  inputElement.value = value;
  inputElement.dispatchEvent(new Event("input"));
}

export function wait(millisecs: number): Promise<void> {
  return new Promise((r) => setTimeout(r, millisecs));
}
