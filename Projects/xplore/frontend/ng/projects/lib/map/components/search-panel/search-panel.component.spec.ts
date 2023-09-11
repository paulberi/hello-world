import {async, ComponentFixture, TestBed} from "@angular/core/testing";
import {SearchPanelComponent} from "./search-panel.component";
import {MatSelectModule} from "@angular/material/select";
import {MatRadioModule} from "@angular/material/radio";
import {CollapsablePanelComponent} from "../collapsable-panel/collapsable-panel.component";
import {CollapseButtonComponent} from "../collapsable-panel/collapse-button.component";
import {MatIconModule} from "@angular/material/icon";
import {FormsModule} from "@angular/forms";
import { SelectionService } from "../../services/selection.service";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

jest.mock('../../services/selection.service');

describe('Component: SearchPanelComponent', () => {

  let component: SearchPanelComponent;
    let fixture: ComponentFixture<SearchPanelComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                FormsModule,
                MatRadioModule,
                MatIconModule,
                MatSelectModule,
                NoopAnimationsModule
            ],
            providers: [
                SelectionService
            ],
            declarations: [
                CollapsablePanelComponent,
                CollapseButtonComponent,
                SearchPanelComponent
            ]
        }).compileComponents();
        fixture = TestBed.createComponent(SearchPanelComponent);
        component = fixture.debugElement.componentInstance;
        fixture.detectChanges();
    }));

    it('should create SearchPanelComponent', () => {
        expect(component).toBeTruthy();
        expect(component.collapsed).toBeFalsy();
    });

    it('should be collapsed on small screen', () => {
        expect(fixture.componentInstance.collapsed).toBeFalsy();
        (<any>window).innerWidth = 659;
        fixture.componentInstance.ngOnInit();
        fixture.detectChanges();
        expect(fixture.componentInstance.collapsed).toBeTruthy();
    });

});
