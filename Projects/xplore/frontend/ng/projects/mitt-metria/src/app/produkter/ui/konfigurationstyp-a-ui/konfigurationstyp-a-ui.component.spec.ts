import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KonfigurationstypAUiComponent } from './konfigurationstyp-a-ui.component';
import { MMKonfigurationstypAUiModule } from './konfigurationstyp-a-ui.module';

describe('KonfigurationstypAUiComponent', () => {
  let component: KonfigurationstypAUiComponent;
  let fixture: ComponentFixture<KonfigurationstypAUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KonfigurationstypAUiComponent],
      imports: [MMKonfigurationstypAUiModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(KonfigurationstypAUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});
