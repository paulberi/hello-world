import { Component, ViewChild } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { XpPage } from './page';

import { XpPaginatedTableComponent } from './paginated-table.component';

class TableTestObject {
  col1: string;
  col2: string;
}

@Component({
  template: `
    <xp-paginated-table [page]="page" [pageSizeOptions] = "pageSizeOptions">
      <ng-container matColumnDef="col1-def">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>col1-header</th>
        <td mat-cell *matCellDef="let elem">
          <span>elem.col1</span>
        </td>
      </ng-container>
      <ng-container matColumnDef="col2-def">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>col2-header</th>
        <td mat-cell *matCellDef="let elem">
          <span>elem.col2</span>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
    </xp-paginated-table>
  `,
})
class WrapperComponent {
  @ViewChild(XpPaginatedTableComponent, { static: true }) appComponentRef: XpPaginatedTableComponent;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  readonly displayedColumns = ["col1-def", "col2-def"];
  readonly pageSizeOptions = [15, 30, 45];

  page: XpPage<TableTestObject>;

  constructor() {
    this.page = new XpPage<TableTestObject>();

    this.page.content = [
      {
        col1: "row1 col1",
        col2: "row1 col2"
      },
      {
        col1: "row2 col1",
        col2: "row2 col2"
      }
    ];
    this.page.number = 1;
    this.page.totalElements = 42;
  }
}

describe("XpPaginatedTableComponent", () => {
  let component: XpPaginatedTableComponent;
  let fixture: ComponentFixture<WrapperComponent>;
  let wrapper: WrapperComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XpPaginatedTableComponent, WrapperComponent ],
      imports: [
        MatTableModule,
        MatPaginatorModule,
        NoopAnimationsModule
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WrapperComponent);
    wrapper = fixture.debugElement.componentInstance;
    component = wrapper.appComponentRef;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("ska kunna visa en tabell", () => {
    expect(component.columnDefs.map(cd => cd.name))
      .toEqual(wrapper.displayedColumns);

    expect(component.rowDefs.length).toEqual(1);
    expect(Array.from(component.rowDefs.first.columns))
      .toEqual(wrapper.displayedColumns);

    expect(component.headerRowDefs.length).toEqual(1);
    expect(Array.from(component.headerRowDefs.first.columns))
      .toEqual(wrapper.displayedColumns);

    expect(component.table.dataSource).toEqual(wrapper.page.content);
  });

  it("ska kunna paginera", () => {
    let spy = jest.spyOn(component.pageChange, "emit");

    let paginator = fixture.debugElement.query(By.directive(MatPaginator)).context as MatPaginator;
    paginator.nextPage();

    expect(spy).toHaveBeenCalledTimes(1);
    expect(spy).toHaveBeenCalledWith({
      length: component.page.totalElements,
      pageIndex: component.page.number + 1,
      pageSize: component.pageSizeOptions[0],
      previousPageIndex: component.page.number
    });
  })
});
