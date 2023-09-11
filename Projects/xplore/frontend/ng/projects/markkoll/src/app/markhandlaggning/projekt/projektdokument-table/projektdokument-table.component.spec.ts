import { HttpClientTestingModule } from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OAuthService, OAuthStorage } from "angular-oauth2-oidc";
import { DokumentService } from "../../../services/dokument.service";

import { ProjektdokumentTableComponent } from './projektdokument-table.component';

describe('ProjektdokumentTableComponent', () => {
  let component: ProjektdokumentTableComponent;
  let fixture: ComponentFixture<ProjektdokumentTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ],
      imports: [
        ProjektdokumentTableComponent,
        HttpClientTestingModule
      ],
      providers: [
        { provide: DokumentService, useValue: {} }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjektdokumentTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
