import VectorLayer from "ol/layer/Vector";
import { Observable, Subject, Subscription } from "rxjs";
import { convertFeature } from "../../../../../../common/geometry-util";
import { MkMapComponent } from "../../../../../../common/map/map.component";
import { IntrangOption } from "../intrang-form/intrang-form.component";
import { MkIntrangVerktyg } from "../intrang-verktyg";

export class SelectIntrangVerktyg implements MkIntrangVerktyg<IntrangOption[]> {

  private selectionChangeSubscription: Subscription;
  private selectIntrangSubject = new Subject<IntrangOption[]>();

  constructor(private mapComponent: MkMapComponent,
    private linjeLayer: VectorLayer<any>,
    private punktLayer: VectorLayer<any>) { }

  get verktygUpdate$(): Observable<IntrangOption[]> {
    return this.selectIntrangSubject.asObservable();
  }

  activate(): void {
    this.punktLayer.set("map_selectable", true);
    this.linjeLayer.set("map_selectable", true);

    this.selectionChangeSubscription = this.mapComponent.selectionChange.subscribe(features => {
      const selectedFeatures = features
        .filter(f => f.layerId === this.linjeLayer.get("id") || f.layerId === this.punktLayer.get("id"))
        .map(f => f.features)
        .flatMap(f => f);

      const intrangOptions: IntrangOption[] = selectedFeatures.map(sf =>
        convertFeature(sf.getProperties()["id"], sf)
      );

      this.selectIntrangSubject.next(intrangOptions);
    });
  }

  inactivate(): void {
    this.punktLayer.set("map_selectable", false);
    this.linjeLayer.set("map_selectable", false);

    this.reset();
    this.selectionChangeSubscription?.unsubscribe();
    this.selectionChangeSubscription = null;
  }

  reset() {
    this.mapComponent.clearSelection();
    this.selectIntrangSubject.next([]);
  }
}
