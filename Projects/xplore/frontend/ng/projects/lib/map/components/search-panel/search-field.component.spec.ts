import {async, ComponentFixture, TestBed} from "@angular/core/testing";
import {SearchFieldComponent} from "./search-field.component";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import "jest";
import {ChangeDetectionStrategy} from "@angular/core";
//For linting purposes we want jest and not jasmine

describe('Component: SearchFieldComponent', () => {
    let component: SearchFieldComponent;
    let fixture: ComponentFixture<SearchFieldComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [MatAutocompleteModule, MatIconModule, MatFormFieldModule, ReactiveFormsModule, MatInputModule, NoopAnimationsModule],
            declarations: [
                SearchFieldComponent
            ]
        }).overrideComponent(SearchFieldComponent, {
            set: { changeDetection: ChangeDetectionStrategy.Default },
        }).compileComponents();
        fixture = TestBed.createComponent(SearchFieldComponent);
        component = fixture.debugElement.componentInstance;
        fixture.detectChanges();
    }));

    it('should create SearchFieldComponent', () => {
        expect(component).toBeTruthy();
    });

    // it('should have correct placeholder', () => {
    //     component.placeholder = "ABC123";
    //     fixture.detectChanges();
    //     let inputElement = fixture.nativeElement.querySelector("input");
    //     expect(inputElement.placeholder).toBe("ABC123");
    // });
});
