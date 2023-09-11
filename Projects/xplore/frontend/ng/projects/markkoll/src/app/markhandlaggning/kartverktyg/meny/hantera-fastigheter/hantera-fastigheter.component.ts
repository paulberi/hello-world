import { Component, Input, OnInit, TemplateRef, ViewChild } from '@angular/core';
import VectorSource from 'ol/source/Vector';
import { FastighetDelomradeInfo } from '../../../../../../../../generated/markkoll-api';
import { uuid } from '../../../../model/uuid';
import GeoJSON from "ol/format/GeoJSON";
import Feature from 'ol/Feature';
import Geometry from 'ol/geom/Geometry';
import { Observable } from 'rxjs';
import VectorLayer from 'ol/layer/Vector';
import { StyleFunction } from 'ol/style/Style';
import { MatDialog } from '@angular/material/dialog';
import { hanteraFastighetStyle } from './hantera-fastigheter-style';
import { MkKartmenyvalDelegate } from '../default-kartverktyg-delegate';

enum SelectedStatus {
  ADDED = "added",
  REMOVED = "removed",
  CURRENT = "current",
  HIDDEN = "hidden"
}

enum FeatureProperties {
  SELECTED_STATUS = "map_tool_selected_status",
  OBJEKT_ID = "objekt_id"
}

@Component({
  selector: 'mk-hantera-fastigheter',
  templateUrl: './hantera-fastigheter.component.html',
  styleUrls: ['./hantera-fastigheter.component.scss']
})
export class MkHanteraFastigheterComponent implements OnInit {
  @Input() delegate: MkHanteraFastigheterDelegate;

  @ViewChild("cancelDialog") cancelDialog: TemplateRef<any>;

  private features: Feature<Geometry>[] = [];

  currentFastigheter = new Map<uuid, string>();
  addedFastigheter = new Map<uuid, string>();
  removedFastigheter = new Map<uuid, string>();
  hiddenFastigheter = new Map<uuid, string>();

  addedFastighetsbeteckningar: [string, string][] = [];
  removedFastighetsbeteckningar: [string, string][] = [];

  constructor(private matDialog: MatDialog) {}

  ngOnInit(): void {
    this.setupTool();

    this.delegate.getFastighetOnClick()
      .subscribe((fastighetId: uuid) => {
        this.fastighetSelected(fastighetId);
      });
  }

  private setupTool() {

    this.delegate.getDelomradenForProjekt()
      .subscribe(delomraden => {

        this.currentFastigheter.clear();
        this.addedFastigheter.clear();
        this.removedFastigheter.clear();

        delomraden.forEach(omrade => {
          if(!this.currentFastigheter.has(omrade.fastighetId)){
            this.currentFastigheter.set(omrade.fastighetId, omrade.fastighetsbeteckning);
          }
        });

        this.setupMap();
        this.features = [];
        this.addDelomradenToFeatures(delomraden, SelectedStatus.CURRENT);
      });
  }

  private setupMap() {
    const layer = this.delegate.getLayer();
    const vectorSource = new VectorSource({});
    layer.setSource(vectorSource);
    layer.setStyle(hanteraFastighetStyle as StyleFunction);
  }

  private updateFeatureStatus(fastighetId: uuid, selectedStatus: SelectedStatus) {
    const fastighetFeatures = this.features.filter(feature => feature.get(FeatureProperties.OBJEKT_ID) === fastighetId);
    fastighetFeatures.forEach(feature => feature.set(FeatureProperties.SELECTED_STATUS, selectedStatus));
    this.updateFastighetsbeteckningarLists();
  }

  save() {
    const added = this.getFastighetIdBySelectedStatus(SelectedStatus.ADDED);
    const removed = this.getFastighetIdBySelectedStatus(SelectedStatus.REMOVED);

    this.delegate.saveChanges(added, removed)
      .subscribe(() => {
        this.setupTool()
      });
  }

  cancel() {
   this.matDialog.open(this.cancelDialog)
    .afterClosed()
      .subscribe(res => {
        if (res){
          this.setupTool();
        }
      })
  }

  fastighetSelected(fastighetId: uuid) {
    if(this.addedFastigheter.has(fastighetId)) {
      this.hiddenFastigheter.set(fastighetId, this.addedFastigheter.get(fastighetId))
      this.addedFastigheter.delete(fastighetId);
      this.updateFeatureStatus(fastighetId, SelectedStatus.HIDDEN);
    }
    else if(this.removedFastigheter.has(fastighetId)) {
      this.removedFastigheter.delete(fastighetId);
      this.updateFeatureStatus(fastighetId, SelectedStatus.CURRENT);
    }
    else if(this.currentFastigheter.has(fastighetId)) {
      this.removedFastigheter.set(fastighetId, this.currentFastigheter.get(fastighetId));
      this.updateFeatureStatus(fastighetId, SelectedStatus.REMOVED);
    }
    else if(this.hiddenFastigheter.has(fastighetId)) {
      this.addedFastigheter.set(fastighetId, this.hiddenFastigheter.get(fastighetId));
      this.updateFeatureStatus(fastighetId, SelectedStatus.ADDED);
    }
    else {
      this.delegate.getDelomradenForFastighet(fastighetId)
        .subscribe(delomraden => {
          if(delomraden.length > 0){
            this.addDelomradenToFeatures(delomraden, SelectedStatus.ADDED);
            this.addedFastigheter.set(fastighetId, delomraden[0].fastighetsbeteckning);
            this.updateFastighetsbeteckningarLists();
          }
        });
    }

  }

  private updateFastighetsbeteckningarLists() {
    this.addedFastighetsbeteckningar = Array.from(this.addedFastigheter.entries()).sort((a, b) => (a[1] > b[1]) ? 1 : -1);
    this.removedFastighetsbeteckningar = Array.from(this.removedFastigheter.entries()).sort((a, b) => (a[1] > b[1]) ? 1 : -1);
  }

  private getFastighetIdBySelectedStatus(selectedStatus: SelectedStatus): uuid[] {
    return this.features
    .filter(feature => feature.get(FeatureProperties.SELECTED_STATUS) === selectedStatus)
    .map(feature => feature.get(FeatureProperties.OBJEKT_ID))
    .filter(this.onlyUnique);
  }

  private onlyUnique(value: any, index: number, self: any) {
    return self.indexOf(value) === index;
  }

  private convertGeoJSONToFeature(geoJson: string): Feature<Geometry>[] {
    return new GeoJSON().readFeatures(geoJson);
  }

  private addDelomradenToFeatures(delomraden: FastighetDelomradeInfo[], selectedStatus: string) {
    const newFeatures = [];

    delomraden.forEach(delomrade => {
      const features = this.convertGeoJSONToFeature(delomrade.geometry);
      features.forEach(feature => {
        feature.set(FeatureProperties.SELECTED_STATUS, selectedStatus);
        feature.set(FeatureProperties.OBJEKT_ID, delomrade.fastighetId);
      });
      newFeatures.push(...features);
    });

    this.features.push(...newFeatures);
    const layer = this.delegate.getLayer();
    layer.getSource().addFeatures(newFeatures);
  }
}

export interface MkHanteraFastigheterDelegate extends MkKartmenyvalDelegate {
  // karta
  getLayer(): VectorLayer<any>;
  getFastighetOnClick(): Observable<uuid>;

  // datamodell
  getDelomradenForProjekt(): Observable<FastighetDelomradeInfo[]>;
  getDelomradenForFastighet(fastighetsId: uuid): Observable<FastighetDelomradeInfo[]>;
  saveChanges(addedFastigheter: uuid[], removedFastigheter: uuid[]): Observable<void>;
}
