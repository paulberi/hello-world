import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatOptionModule } from "@angular/material/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { MatSelectModule } from "@angular/material/select";
import { MatSortModule } from "@angular/material/sort";
import { MatTableModule } from "@angular/material/table";
import { of } from "rxjs";
import { MkProjektlistaComponent } from "./projektlista.component";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterTestingModule } from "@angular/router/testing";
import { XpPage } from "../../../../../../lib/ui/paginated-table/page";
import { mockProjektInfo } from "../../../../test/data";
import { XpPaginatedTableComponent } from "../../../../../../lib/ui/paginated-table/paginated-table.component";
import { queryComponent, setInputValueBySelector } from "../../../../test/jest-util";
import { ProjektService } from "../../../services/projekt.service";
import { XpTranslocoTestModule } from "../../../../../../lib/translate/translocoTest.module.translate";
import { XpPaginatedTableModule } from "../../../../../../lib/ui/paginated-table/paginated-table.module";
import { MkCommonModule } from "../../../common/common.module";
import { ProjektInfo } from "../../../../../../../generated/markkoll-api";
import { MkPipesModule } from "../../../common/pipes/pipes.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";


describe("ProjektlistaComponent", () => {
  let component: MkProjektlistaComponent;
  let fixture: ComponentFixture<MkProjektlistaComponent>;
  let mockProjektService: any;

  beforeEach(async () => {
    mockProjektService = {
      getProjektPage: jest.fn()
        .mockReturnValueOnce(of({})) // returvärde från anrop i ngOnInit
        .mockReturnValue(of(mockPage()))
    };

    await TestBed.configureTestingModule({
      declarations: [
        MkProjektlistaComponent,
      ],
      providers: [
        { provide: ProjektService, useValue: mockProjektService }
      ],
      imports: [
        MatPaginatorModule,
        ReactiveFormsModule,
        MatSortModule,
        MatFormFieldModule,
        MatOptionModule,
        MatIconModule,
        MatSelectModule,
        MatTableModule,
        MatInputModule,
        XpTranslocoTestModule,
        RouterTestingModule,
        XpPaginatedTableModule,
        MkCommonModule,
        MkPipesModule,
        NoopAnimationsModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MkProjektlistaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // anrop i ngOnInit
    expect(mockProjektService.getProjektPage).toHaveBeenCalledTimes(1);
    mockProjektService.getProjektPage.mockClear();

    component.pageIndex = 1;
    component.pageSize = 15;
    component.direction = "asc";
    component.active = "skapadDatum";
    component.search = "Projekt 123";

    expect(queryComponent(fixture, XpPaginatedTableComponent).page).toEqual({});
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska visa ny sida vid sortering av kolumner", async () => {
    // Given
    const sortableColumns = ["namn", "skapadDatum"];

    // Vänta på att tabellen ska renderas klart
    await fixture.whenStable();

    const tableHeaders = fixture.nativeElement.querySelectorAll(".mat-sort-header-container");
    expect(tableHeaders.length).toBe(sortableColumns.length);

    sortableColumns.forEach((column, i) => {
      // When
      tableHeaders[i].click();
      fixture.detectChanges();

      // Then
      expect(mockProjektService.getProjektPage).toHaveBeenCalledTimes(1);
      expect(mockProjektService.getProjektPage).toHaveBeenCalledWith(
        component.pageIndex,
        component.pageSize,
        component.direction,
        column,
        component.search);

      const paginatedTable = queryComponent(fixture, XpPaginatedTableComponent);
      expect(paginatedTable.page).toEqual(mockPage());

      mockProjektService.getProjektPage.mockClear();
    });
  });

  it("Ska visa ny sida vid paginering", async () => {
    // Given
    const paginatedTable = queryComponent(fixture, XpPaginatedTableComponent);
    const pageIndex = 2;
    const pageSize = 5;

    // When
    paginatedTable.pageChange.emit({
      pageIndex: pageIndex,
      pageSize: pageSize,
    } as PageEvent);

    fixture.detectChanges();
    await fixture.whenStable();

    // Then
    expect(mockProjektService.getProjektPage).toHaveBeenCalledTimes(1);
    expect(mockProjektService.getProjektPage).toHaveBeenCalledWith(
      pageIndex,
      pageSize,
      component.direction,
      component.active,
      component.search);

    expect(paginatedTable.page).toEqual(mockPage());
  });

  it("Ska visa ny sida vid uppdaterad sökning", async () => {
    // Given
    const NAMN = "Projektnamn 456";

    // When
    setInputValueBySelector(fixture, NAMN, "input");

    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    // Then
    expect(mockProjektService.getProjektPage).toHaveBeenCalledTimes(1);
    expect(mockProjektService.getProjektPage).toHaveBeenCalledWith(
      component.pageIndex,
      component.pageSize,
      component.direction,
      component.active,
      NAMN);

    const paginatedTable = queryComponent(fixture, XpPaginatedTableComponent);
    expect(paginatedTable.page).toEqual(mockPage());
  });
});

function mockPage(): XpPage<ProjektInfo> {
  const content = [mockProjektInfo(), mockProjektInfo()];

  const page = new XpPage<ProjektInfo>();
  page.content = content;
  page.numberOfElements = content.length;
  page.totalElements = 123;
  page.totalPages = 13;

  return page;
}
