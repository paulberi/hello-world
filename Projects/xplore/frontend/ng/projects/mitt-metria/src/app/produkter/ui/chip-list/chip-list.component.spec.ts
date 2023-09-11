import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChipListComponent } from './chip-list.component';
import { MMChipListModule } from './chip-list.module';


describe('ChipListComponent', () => {
  let component: ChipListComponent;
  let fixture: ComponentFixture<ChipListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChipListComponent ],
      imports: [MMChipListModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChipListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
