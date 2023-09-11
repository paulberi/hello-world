import {AfterViewInit, Component, EventEmitter, HostListener, OnInit, Output, ViewChild} from "@angular/core";
import {ExportPaperMaskComponent} from "./export-paper-mask.component";
import {MapService} from "../../map-core/map.service";
import {ExportRequest, ExportService} from "./export.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {HttpErrorResponse} from "@angular/common/http";

/**
 * This component contains the entire UI for the export function.
 *
 * It is normally dynamically instantiated (i.e. not via a template). See ExportUiService.
 */
@Component({
  selector: "xp-export-ui",
  template: `
    <!-- Because the background is generally a transparent white, we have to force a light theme for text legibility. -->
    <div class="metria-light-theme overlay">
      <xp-export-paper-mask #paper [size]="exportRequest.paperSize" [orientation]="exportRequest.orientation"
                            [margins]="getMargins()">
        <form class="above-paper" autocomplete="off">
          <mat-form-field class="title" [hintLabel]="'Max ' + maxTitleLength + ' tecken'">
            <input [(ngModel)]="exportRequest.title" name="export-title" [maxlength]="maxTitleLength" matInput/>
            <mat-hint align="end">{{exportRequest.title.length}}/{{maxTitleLength}}</mat-hint>
            <mat-placeholder class="placeholder">Titel</mat-placeholder>
          </mat-form-field>
          <mat-form-field class="scale">
            <input [(ngModel)]="scale" (change)="onScaleChange()" name="scale" matInput type="number"/>
            <mat-placeholder class="placeholder">Skala</mat-placeholder>
            <span matPrefix>1:</span>
          </mat-form-field>
        </form>
        <form class="below-paper">
          <div class="paper-settings" *ngIf="staticOrientation == null">
            <mat-form-field class="mat-form-field-narrow paper-size">
              <mat-select [(ngModel)]="exportRequest.paperSize" (ngModelChange)="onPaperChange()" name="paper-size">
                <mat-option value="a4">A4</mat-option>
                <mat-option value="a3">A3</mat-option>
              </mat-select>
              <mat-placeholder class="placeholder">Pappersstorlek</mat-placeholder>
            </mat-form-field>
            <mat-form-field class="mat-form-field-narrow orientation">
              <mat-select [(ngModel)]="exportRequest.orientation" (ngModelChange)="onPaperChange()" name="orientation">
                <mat-option value="landscape">Liggande</mat-option>
                <mat-option value="portrait">Stående</mat-option>
              </mat-select>
              <mat-placeholder class="placeholder">Orientering</mat-placeholder>
            </mat-form-field>
          </div>
          <div class="actions">
            <!-- For some reason [(ngModel)] doesn't work here. It probably has to do with the class selector used in
                 xp-export-paper-mask. It's probably a bug in angular material. -->
            <div  *ngIf="staticOrientation == null">
              <mat-radio-group [value]="exportRequest.format" (change)="exportRequest.format = $event.value"
                               aria-label="Välj format" name="exportFormat">
                <mat-radio-button value="PDF">PDF</mat-radio-button>
                <mat-radio-button value="PNG">PNG</mat-radio-button>
              </mat-radio-group>
            </div>
            <div>
              <button mat-button (click)="cancelClick.emit()">Stäng</button>
              <button mat-raised-button color="accent" (click)="export()">{{exportButtonText}}</button>
            </div>

            <!-- Div som används av exporten, en skugg-karta ansluts till den för exporten -->
            <div id="exportShadowMap"></div>
          </div>
        </form>
        <div *ngIf="exporting" class="over-paper">
          <div class="spinner">
            <mat-spinner [diameter]="64"></mat-spinner>
            <div>Exporterar...</div>
          </div>
        </div>
      </xp-export-paper-mask>
    </div>
  `,
  styles: [`
    .overlay {
      pointer-events: none;
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      z-index: 1;
    }

    .placeholder {
      color: rgb(0, 0, 0);
    }

    form {
      display: flex;
      flex-direction: column;
    }

    form.above-paper {
      flex-direction: row;
    }

    form.above-paper .title {
      flex-grow: 2;
      width: 1px;
    }

    form.above-paper .scale {
      flex-grow: 1;
      width: 1px;
    }

    form .paper-settings {
      display: flex;
      flex-direction: row;
      margin-top: 5px;
    }

    form .paper-settings mat-form-field {
      flex-grow: 1;
    }

    form .paper-settings .mat-form-field-infix {
      flex-grow: 1;
    }

    form .actions {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
    }

    form .actions button {
      margin-left: 5px;
    }

    form .actions mat-radio-button {
      margin-right: 5px;
    }

    input[number] {
      width: 100px;
    }

    .over-paper {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .spinner {
      background: white;
      padding: 20px 40px;
      border-radius: 5px;
    }

    .spinner mat-spinner {
      margin: 5px;
    }
  `]
})
export class ExportUiComponent implements AfterViewInit, OnInit {
  @Output() cancelClick = new EventEmitter();

  @ViewChild("paper") paper: ExportPaperMaskComponent;

  scale = "0";

  maxTitleLength = 200;

  exportRequest: ExportRequest = {
    paperSize: "a4",
    orientation: "landscape",
    title: "",
    format: "PDF",
    dpi: 150,
    features: [],
    highlightFeatures: false,
    filename: "Ny utskrift"
  };

  public staticOrientation: "portrait" | "landscape";

  public exportButtonText;

  exporting = false;
  appName: string;
  exportCallback: (exportRequest: ExportRequest) => Promise<void>;

  constructor(private mapService: MapService, private exportService: ExportService,
              private snackBar: MatSnackBar) {
  }

  export() {
    const rect = this.paper.getBoundingClientRect();
    this.exportRequest.extent = [
      ...this.mapService.map.getCoordinateFromPixel([rect.left, rect.bottom]),
      ...this.mapService.map.getCoordinateFromPixel([rect.right, rect.top])
    ];
    const scale = this.computeScale();
    this.exportRequest.footer = "1:" + new Intl.NumberFormat("sv-SE").format(Math.round(scale));

    this.exportRequest.resizeAndSplitTitleIfNecessary = true;

    this.exporting = true;

    if (this.exportCallback != null) {
      this.exportCallback(this.exportRequest).then(() => {
        this.exporting = false;
      }, (err) => {
        console.error(err);

        if (err instanceof HttpErrorResponse) {
          this.snackBar.open("Fel: HTTP " + err.status + " " + err.statusText, "OK", {
            verticalPosition: "top"
          });
        } else {
          this.snackBar.open("Fel: " + err, "OK", {
            verticalPosition: "top"
          });
        }

        this.exporting = false;
      });
    } else {
      this.exportService.export(this.exportRequest, this.appName).then(() => {
        this.exporting = false;
      }, () => {
        this.exporting = false;
      });
    }
  }

  ngOnInit() {
    if (this.staticOrientation != null) {
      this.exportRequest.paperSize = "a4";
      this.exportRequest.format = "PDF";
      this.exportRequest.orientation = this.staticOrientation;
    }
  }

  ngAfterViewInit(): void {
    this.onPaperChange();
    this.mapService.map.getView().on("change:resolution", (() => {
      this.updateScale();
    }));
  }

  /**
   * Called when paper settings have changed (size, orientation).
   * That means that the scale has to be recomputed.
   *
   * It is also called when the browser window resizes, because that means that the paper has likely
   * changed size as well.
   */
  @HostListener("window:resize")
  onPaperChange() {
    // Because our calculations also depend on DOM state changing due to paper settings, doing the calculations
    // immediately doesn't work.
    setTimeout(() => {
      this.updateScale();
    }, 0);
  }

  /**
   * Called when the scale has been *manually* changed in the input field.
   * This means that the maps resolution has to be updated. This is the opposite calculation
   * of what happens when the maps resolution changes.
   */
  onScaleChange() {
    const newScale = parseInt(this.scale);

    const pixelWidth = this.paper.getBoundingClientRect().width;
    const paperWidth = this.paper.getPaperDimensions()[0];
    const resolution = newScale / (pixelWidth / paperWidth);

    this.mapService.map.getView().setResolution(resolution);

  }

  /**
   * Get the page margins in meters. A PNG image has no margins, but a PDF page might have.
   */
  getMargins() {
    if (this.exportRequest.format === "PDF") {
      const pdfMargins = this.exportService.getPdfMargins();
      return [pdfMargins[0] / 1000, pdfMargins[1] / 1000];
    } else {
      return [0, 0];
    }
  }

  /**
   * Update the text field with the current scale.
   */
  private updateScale() {
    const scale = this.computeScale();
    if (scale != null) {
      this.scale = "" + Math.round(scale);
    }
  }

  private computeScale() {
    if (this.paper == null) {
      return null;
    }
    const resolution = this.mapService.map.getView().getResolution();
    const pixelWidth = this.paper.getBoundingClientRect().width;
    const paperWidth = this.paper.getPaperDimensions()[0];

    return (pixelWidth * resolution) / paperWidth;
  }
}
