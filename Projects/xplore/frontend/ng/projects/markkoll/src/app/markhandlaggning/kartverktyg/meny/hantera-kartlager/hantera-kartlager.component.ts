import { Component, OnInit, AfterViewInit, ViewChild } from "@angular/core";
import { MatSelectionList, MatSelectionListChange } from "@angular/material/list";
import { MatRadioChange } from "@angular/material/radio";
import { TranslocoService } from "@ngneat/transloco";
import { XpErrorService } from "../../../../../../../lib/error/error.service";
import { MkMapComponent } from "../../../../common/map/map.component";
import { MkMapService } from "../../../../services/map.service";
import { MkLegendComponent } from "../../../../common/legend/legend.component";
import { Legend, LegendGroup, LegendItem } from "../../../../common/legend/legend";
import { LayerDef } from "../../../../../../../lib/config/config.service";
import TileWMS from "ol/source/TileWMS";
import ImageWMS from "ol/source/ImageWMS";

export interface LayerItem {
  name: string;
  id: string;
  group: string;
  parentgroup?: string;
}

@Component({
  selector: "mk-hantera-kartlager",
  templateUrl: "./hantera-kartlager.component.html",
  styleUrls: ["./hantera-kartlager.component.scss"]
})

export class MkHanteraKartlagerComponent implements OnInit {
  @ViewChild("selectInfo") selectInfo: MatSelectionList;
  @ViewChild("selectProject") selectProject: MatSelectionList;
  @ViewChild("selectRiksintressen") selectRiksintressen: MatSelectionList;

  layers: LayerItem[] = [
    { name: "Intrång", id: "intrang_projekt", group: "projekt" },
    { name: "Intrång tidigare version", id: "intrangPreviousVersion", group: "projekt" },
    { name: "Buffert", id: "buffert", group: "projekt" },
    { name: "Indata", id: "indata", group: "projekt" }
  ];

  parentGroups: { [group: string]: string } = {
    "riksintressen": "information"
  };

  selectedLayers: { [layerId: string]: boolean } = {
    "intrang_projekt": true,
    "defaultBackground": true
  };

  legend: Legend;

  legendOptions = [
    "fontName:SansSerif",
    "fontAntiAliasing:true",
    "bgColor:0xfafafa",
    "forceTitles:off",
    "forceLabels:on"
  ];

  constructor(private map: MkMapComponent,
              private mapService: MkMapService,
              private translate: TranslocoService,
              private errorService: XpErrorService) {
    this.addLayerGroup("information");
    this.addLayerGroup("riksintressen");
    this.addLayerGroup("bakgrundskartor");
  }

  ngOnInit() {
    this.legend = this.createLegend();
  }

  get informationslager(): LayerItem[] {
    return this.getLayerGroup("information");
  }

  get projektlager(): LayerItem[] {
    return this.getLayerGroup("projekt");
  }

  get bakgrundskartor(): LayerItem[] {
    return this.getLayerGroup("bakgrundskartor");
  }

  get riksintressenlager(): LayerItem[] {
    return this.getLayerGroup("riksintressen");
  }

  getLayerGroup(group: string): LayerItem[] {
    return this.layers.filter(l => l.group === group);
  }

  addLayerGroup(group: string) {
    const layers = this.mapService.getLayerDefs(group);
    this.layers = this.layers.concat(this.convertLayerDefs(layers));
  }

  convertLayerDef(layerDef: LayerDef): LayerItem {
    let layerItem = {
      name: layerDef.title,
      id: layerDef.id,
      group: layerDef.group,
      parentgroup: null
    }

    if (this.parentGroups[layerItem.group]) {
      layerItem.parentgroup = this.parentGroups[layerItem.group];
    }
    return layerItem;
  }

  convertLayerDefs(layerDefs: LayerDef[]): LayerItem[] {
    const layers: LayerItem[] = [];
    layerDefs.forEach(layer => {
      layers.push(this.convertLayerDef(layer));
    });
    return layers;
  }

  setLayerSelected(layerId: string, selected: boolean) {
    this.selectedLayers[layerId] = selected;
    this.setLayerVisible(layerId, selected);
    this.updateLegend();
  }

  getLayerSelected(layerId: string): boolean {
    return this.selectedLayers[layerId] ? this.selectedLayers[layerId] : false;
  }

  allLayersSelected(group: string): boolean {
    const groupLayers = this.layers.filter(l => l.group === group);
    const selected = groupLayers.filter(l => this.getLayerSelected(l.id));
    return selected.length == groupLayers.length;
  }

  someLayersSelected(group: string): boolean {
    const groupLayers = this.layers.filter(l => l.group === group);
    const selected = groupLayers.filter(l => this.getLayerSelected(l.id));
    return selected.length > 0 && selected.length < groupLayers.length;
  }

  onToggleAllInfo(selected: boolean) {
    selected ? this.selectInfo.selectAll() : this.selectInfo.deselectAll();

    this.informationslager.forEach(layer => {
      this.setLayerSelected(layer.id, selected);
    });

    const subgroups = this.layers.filter(l => l.parentgroup === "information");
    subgroups.forEach(layer => {
      this.setLayerSelected(layer.id, selected);
    });
  }

  onToggleAllProject(selected: boolean) {
    selected ? this.selectProject.selectAll() : this.selectProject.deselectAll();

    this.projektlager.forEach(layer => {
      this.setLayerSelected(layer.id, selected);
    });
  }

  onToggleAllRiksintressen(selected: boolean) {
    selected ? this.selectRiksintressen.selectAll() : this.selectRiksintressen.deselectAll();

    this.riksintressenlager.forEach(layer => {
      this.setLayerSelected(layer.id, selected);
    });
  }

  selectionChange(change: MatSelectionListChange) {
    const option = change.options[0];
    this.setLayerSelected(option.value, option.selected);
  }

  onBackgroundSelected(change: MatRadioChange) {
    this.map.switchBackgroundLayer(change.value);
  }

  layerVisible(layerId: string): boolean {
    const layer = this.map.getLayer(layerId);
    return layer?.getVisible();
  }

  setLayerVisible(layerId: string, visible: boolean) {
    const layer = this.map.getLayer(layerId);
    if (layer) {
      layer.setVisible(visible);
    }
  }

  updateLegend() {
    this.legend = this.createLegend();
  }

  createLegend(): Legend {
    let legend: Legend = {
      groups: []
    }

    const informationTitle = this.translate.translate("mk.kartverktyg.hanteraKartlager.infoLayers");
    let group = this.createLegendGroup(informationTitle, this.informationslager);
    legend.groups.push(group);

    const riksintressenTitle = this.translate.translate("mk.kartverktyg.hanteraKartlager.riksintressenLayers");
    group = this.createLegendGroup(riksintressenTitle, this.riksintressenlager);
    legend.groups.push(group);

    return legend;
  }

  createLegendGroup(title: string, layers: LayerItem[]): LegendGroup {
    let group = {
      title: title,
      items: []
    }

    layers.forEach(layer => {
      try {
        if (this.getLayerSelected(layer.id)) {
          group.items.push(this.getLegendItem(layer.id, layer.name));
        }
      } catch (e) {
        this.errorService.notifyError(e);
      }
    });

    return group;
  }

  getLegendItem(layerId: string, name: string): LegendItem {
    const layer = this.map.getLayer(layerId);

    if (!layer) {
      throw new Error("Layer not found: " + layerId);
    }

    const layerSource = layer.getSource();
    if (layerSource instanceof TileWMS || layerSource instanceof ImageWMS) {
      let legendUrl = layerSource.getLegendUrl(
        undefined,
        {
          legend_options: this.legendOptions.join(";")
        }
      );

      let item: LegendItem = {
        title: name,
        iconUrl: legendUrl
      }
      return item;
    } else {
      throw new Error("Unhandled layer type.");
    }
  }
}
