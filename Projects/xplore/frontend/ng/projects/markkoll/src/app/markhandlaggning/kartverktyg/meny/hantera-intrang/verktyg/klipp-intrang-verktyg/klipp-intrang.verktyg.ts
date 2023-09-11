import { Feature, Map } from "ol";
import VectorSource from "ol/source/Vector";
import { Observable, Subject } from "rxjs";
import Split from "ol-ext/interaction/Split";
import BaseEvent from "ol/events/Event";
import uuid from "uuid-random";
import { MkIntrangVerktyg } from "../intrang-verktyg";
import { convertFeature } from "../../../../../../common/geometry-util";
import { KlippIntrangEvent } from "./klipp-intrang-event";

export class KlippIntrangVerktyg implements MkIntrangVerktyg<KlippIntrangEvent> {
  private split: Split;
  private splitEvent: SplitEvent;

  private klippIntrangSubject = new Subject<KlippIntrangEvent>();

  constructor(private map: Map,
              private source: VectorSource) {}

  activate(): void {
    this.inactivate();

    this.split = new Split({
      sources: this.source,
      snapDistance: 5
    });
    this.map.addInteraction(this.split);

    this.map.on("singleclick", e => {
      if (!this.split.getActive()) {
        this.undo();
        this.split.dispatchEvent(e);
      }
    });

    this.split.addEventListener("aftersplit", e => {
      this.splitEvent = e as SplitEvent;
      this.split.setActive(false);

      const originalFeature = this.splitEvent.original;
      const selectedFeature1 = this.splitEvent.features[0];
      const selectedFeature2 = this.splitEvent.features[1];

      selectedFeature1.set("cut_selected_1", true);
      selectedFeature2.set("cut_selected_2", true);

      const originalOption = convertFeature(originalFeature.get("id"), originalFeature);
      const selectedOption1 = convertFeature(uuid(), selectedFeature1);
      const selectedOption2 = convertFeature(uuid(), selectedFeature2);

      this.klippIntrangSubject.next({
        original: originalOption,
        klippta: [selectedOption1, selectedOption2]
      })
    });
  }

  inactivate(): void {
    this.undo();
    this.map.removeInteraction(this.split);
  }

  get verktygUpdate$(): Observable<KlippIntrangEvent> {
    return this.klippIntrangSubject.asObservable();
  }

  private undo() {
    if (this.splitEvent) {
      this.splitEvent.features.forEach(f => this.source.removeFeature(f));
      this.source.addFeature(this.splitEvent.original);

      this.reset();
    }
  }

  reset() {
    if (this.splitEvent) {
      this.splitEvent = null;
      this.split.setActive(true);
    }
    this.klippIntrangSubject.next({
      original: null,
      klippta: []
    });
  }
}

class SplitEvent extends BaseEvent {
  original: Feature;
  features: Feature[];
}
