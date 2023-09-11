import {CollapseButtonComponent} from "./collapse-button.component";
import {async, ComponentFixture, TestBed} from "@angular/core/testing";
import {MatIcon} from "@angular/material/icon";

describe('Component: CollapseButtonComponent', () => {
    let component: CollapseButtonComponent;
    let fixture: ComponentFixture<CollapseButtonComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                MatIcon,
                CollapseButtonComponent
            ]
        }).compileComponents();
        fixture = TestBed.createComponent(CollapseButtonComponent);
        component = fixture.debugElement.componentInstance;
        fixture.detectChanges();
    }));

    it('should create CollapseButtonComponent', async(() => {
        expect(component).toBeTruthy();
        expect(component.collapsed).toBeFalsy();
    }));

    it('should have an icon', async(() => {
        component.icon = "i_have_an_icon";
        component.collapsed = true;
        fixture.detectChanges();
        let matIcon = fixture.nativeElement.querySelector("mat-icon");
        expect(matIcon.innerHTML).toBe("i_have_an_icon");
    }));

});