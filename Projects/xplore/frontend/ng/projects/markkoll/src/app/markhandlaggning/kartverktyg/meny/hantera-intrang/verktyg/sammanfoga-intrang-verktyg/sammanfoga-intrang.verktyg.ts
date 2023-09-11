import { Feature } from "ol";
import { Geometry, LineString } from "ol/geom";
import VectorLayer from "ol/layer/Vector";
import { Observable, Subject, Subscription } from "rxjs";
import { IntrangOption } from "../intrang-form/intrang-form.component";
import { MkIntrangVerktyg } from "../intrang-verktyg";
import uuid from "uuid-random";
import { writeGeoJson, mergeLines, convertFeature } from "../../../../../../common/geometry-util";
import { MkMapComponent } from "../../../../../../common/map/map.component";
import { SammanfogaIntrangEvent } from "./sammanfoga-intrang-event";

export class SammanfogaIntrangVerktyg implements MkIntrangVerktyg<SammanfogaIntrangEvent> {
  private selectionChangeSubscription: Subscription;
  private sammanfogaIntrangSubject = new Subject<SammanfogaIntrangEvent>();

  constructor(private mapComponent: MkMapComponent, private linjeLayer: VectorLayer<any>) { }

  activate() {
    this.inactivate();
    this.linjeLayer.set("map_selectable", true);

    this.selectionChangeSubscription = this.mapComponent.selectionChange.subscribe(featureResult => {
      const selectedFeatures = featureResult.map(fr => fr.features).flatMap(f => f);

      const originalOptions: IntrangOption[] = selectedFeatures.map(sf =>
        convertFeature(sf.getProperties()["id"], sf)
      );

      const newOption: IntrangOption = {
        id: uuid(),
        geom: writeGeoJson(this.createLineString(selectedFeatures)),
        intrangstyp: null,
        subtyp: null,
        avtalstyp: null,
      };

      this.sammanfogaIntrangSubject.next({
        original: originalOptions,
        sammanfogat: newOption
      });
    });
  }

  inactivate() {
    this.reset();
    this.linjeLayer.set("map_selectable", false);
    this.selectionChangeSubscription?.unsubscribe();
  }

  reset() {
    this.mapComponent.clearSelection();
    this.sammanfogaIntrangSubject.next({
      original: [],
      sammanfogat: null
    });
  }

  get verktygUpdate$(): Observable<SammanfogaIntrangEvent> {
    return this.sammanfogaIntrangSubject.asObservable();
  }

  private createLineString(features: Feature<Geometry>[]): LineString {
    const SelectedFeatureCoords = features
      .map(f => f.getGeometry() as LineString)
      .map(ls => ls.getCoordinates());
    const merged = mergeLines(SelectedFeatureCoords);

    return new LineString(merged, "XY");
  }
}
