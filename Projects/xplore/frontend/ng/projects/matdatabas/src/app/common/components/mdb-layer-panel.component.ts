import {Component, EventEmitter, Input, Output} from "@angular/core";
import {createLegendUrl} from "../../../../../lib/map-core/style-utils";
import {
  anlaggningStyle,
  berggrundmagasinAktiv, berggrundmagasinInaktiv, energibrunnAktiv, energibrunnInaktiv, grundvattenforekomstStyle,
  infiltration, influensomradeStyle, kansligGrundlaggningStyle,
  matdamm,
  ovreGrundvattenmagasinAktiv,
  ovreGrundvattenmagasinInaktiv, portryckAktiv, portryckInaktiv, provpunkt, pumpgrop, rorelseAktiv, rorelseInaktiv,
  undreGrundvattenmagasinAktiv,
  undreGrundvattenmagasinInaktiv, undreGrundvattenmagasinStyle, vattenkemi, ytvattenAktiv
} from "./styles";
import {ZoomRequestEvent} from "./mdb-layer.component";

const legend = style => createLegendUrl(style.default, [20, 20]);

@Component({
  selector: "mdb-layer-panel",
  template: `
    <section class="layers">
      <h3>Visa lager</h3>
      <ng-container *ngFor="let layerOrGroup of layers">
        <mdb-layer [layer]="layerOrGroup" [isGroupLayer]="isGroup(layerOrGroup)" (zoomRequest)="zoomRequest.emit($event)"></mdb-layer>
        <div *ngIf="isGroup(layerOrGroup)" class="layers-in-group">
          <ng-container *ngFor="let layer of getLayersInGroup(layerOrGroup)">
            <mdb-layer [layer]="layer" (zoomRequest)="zoomRequest.emit($event)"></mdb-layer>
          </ng-container>
        </div>
      </ng-container>
    </section>

    <mat-checkbox [checked]="showLabels" (change)="showLabels=$event.checked; showLabelsChange.emit($event.checked)">
      Mätobjektsnamn i kartan
    </mat-checkbox>

    <section *ngFor="let section of legendSections">
      <h3>{{section.name}}</h3>
      <div class="symbol-container" *ngFor="let symbol of section.symbols">
        <img class="symbol" [src]="symbol.legendUrl">
        {{symbol.title}}
      </div>
    </section>
  `,
  styles: [`
    :host {
      display: block;
      padding: 0.5rem;
    }
    section {
      margin-bottom: 1rem;
    }
    .layers {
      display: grid;
    }
    .layers-in-group {
      margin-left: 1rem;
    }
    mdb-layer {
      margin-bottom: 0.3rem;
    }
    .symbol-container {
      display: flex;
      align-items: center;
      margin-bottom: 0.3rem;
    }
    .symbol {
      margin-right: 5px;
      width: 20px;
    }
  `]
})
export class MdbLayerPanelComponent {
  /** An array of openlayer layers */
  @Input() layers: any[];
  @Output() zoomRequest = new EventEmitter<ZoomRequestEvent>();
  @Input() showLabels: boolean;
  @Output() showLabelsChange = new EventEmitter<boolean>();

  legendSections = [
    {
      name: "Grundvattenmagasin aktiva", symbols: [
        {title: "Undre grundvattenmagasin", legendUrl: legend(undreGrundvattenmagasinAktiv)},
        {title: "Övre grundvattenmagasin", legendUrl: legend(ovreGrundvattenmagasinAktiv)},
        {title: "Berggrundmagasin", legendUrl: legend(berggrundmagasinAktiv)},
        {title: "Portryck", legendUrl: legend(portryckAktiv)},
        {title: "Energibrunn", legendUrl: legend(energibrunnAktiv)}
      ],
    },
    {
      name: "Grundvattenmagasin inaktiva", symbols: [
        {title: "Undre grundvattenmagasin", legendUrl: legend(undreGrundvattenmagasinInaktiv)},
        {title: "Övre grundvattenmagasin", legendUrl: legend(ovreGrundvattenmagasinInaktiv)},
        {title: "Berggrundmagasin", legendUrl: legend(berggrundmagasinInaktiv)},
        {title: "Portryck", legendUrl: legend(portryckInaktiv)},
        {title: "Energibrunn", legendUrl: legend(energibrunnInaktiv)}
      ]
    },
    {
      name: "Tunnelvatten", symbols: [
        {title: "Pumpgrop", legendUrl: legend(pumpgrop)},
        {title: "Mätdamm", legendUrl: legend(matdamm)},
        {title: "Provpunkt", legendUrl: legend(provpunkt)}
      ]
    },
    {
      name: "Vattenkemi", symbols: [
        {title: "Vattenkemi", legendUrl: legend(vattenkemi)}
      ]
    },
    {
      name: "Infiltration", symbols: [
        {title: "Infiltration", legendUrl: legend(infiltration)}
      ]
    },
    {
      name: "Rörelse", symbols: [
        {title: "Dubbar aktiva", legendUrl: legend(rorelseAktiv)},
        {title: "Dubbar inaktiv", legendUrl: legend(rorelseInaktiv)}
      ]
    },
    {
      name: "Ytvattenmätning", symbols: [
        {title: "Ytvattenmätning", legendUrl: legend(ytvattenAktiv)}
      ]
    },
    {
      name: "Övriga lager", symbols: [
        {title: "Anläggning", legendUrl: createLegendUrl(anlaggningStyle, [20, 20])},
        {title: "Influensområde", legendUrl: createLegendUrl(influensomradeStyle, [20, 20])},
        {title: "Känslig grundläggning", legendUrl: createLegendUrl(kansligGrundlaggningStyle, [20, 20])},
        {title: "Undre grundvattenmagasin", legendUrl: createLegendUrl(undreGrundvattenmagasinStyle, [20, 20])},
        {title: "Grundvattenförekomst", legendUrl: createLegendUrl(grundvattenforekomstStyle, [20, 20])}
      ]
    }
  ];

  isGroup(layerOrGroup) {
    return layerOrGroup.get("isGroup");
  }

  getLayersInGroup(group) {
    return group.getLayers().getArray();
  }
}

