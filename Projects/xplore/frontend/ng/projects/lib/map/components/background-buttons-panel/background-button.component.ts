import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {ConfigService, LayerDef} from "../../../config/config.service";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Observable} from "rxjs";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {LayerService} from "../../../map-core/layer.service";

/**
 * Hämtar och visar en thumbnail för aktuellt bakgrundslager.
 *
 * För att auktorisering mot kartservern ska fungera så används angular's HttpClient. Om vi bara sätter src-attributet
 * till en URL så kan vi inte lägga på Authorization-headern.
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-background-button",
  template: `
    <button (click)="layerChange.emit(layer)" mat-raised-button class="background-button"
            [matTooltip]="layer.title"
            matTooltipPosition="above">
      <img *ngIf="thumbnailUrl | async as url" width="60" height="60" [src]="url"/>
    </button>
  `,
  styles: [`
    .background-button {
      line-height: 50px;
      min-width: 60px;
      min-height: 60px;
      padding: 2px 2px;
      margin-right: 5px;
      margin-top: 5px;
    }
  `]
})
export class BackgroundButtonComponent implements OnInit {
  @Input() layer: LayerDef;
  @Output() layerChange = new EventEmitter<LayerDef>();

  thumbnailUrl: Observable<SafeUrl>;

  constructor(private http: HttpClient, private domSanitizer: DomSanitizer, private configService: ConfigService, private layerService: LayerService) {
  }

  ngOnInit(): void {
    // Definierar vilken tile bakgrunden ska bestå av
    // Går inte att hårdkoda eftersom man inte alltid har bakgrundskarta över hela sverige.
    const coord = this.configService.config.thumbnailCoordinates ? this.configService.config.thumbnailCoordinates : this.configService.config.center;
    const resolution = 2;

    const tileUrl = this.layerService.getTileUrl(this.layer, coord, resolution);

    this.thumbnailUrl = this.http.get(tileUrl, {responseType: "blob"}).pipe(
      map(blob => this.domSanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob))));
  }
}
