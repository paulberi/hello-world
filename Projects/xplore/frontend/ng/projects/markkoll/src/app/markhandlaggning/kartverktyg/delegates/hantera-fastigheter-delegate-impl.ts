import VectorLayer from "ol/layer/Vector";
import { forkJoin, Observable, Subject, Subscription } from "rxjs";
import { map } from "rxjs/operators";
import { FastighetDelomradeInfo } from "../../../../../../../generated/markkoll-api";
import { MkMapComponent } from "../../../common/map/map.component";
import { uuid } from "../../../model/uuid";
import { ProjektService } from "../../../services/projekt.service";
import { MkHanteraFastigheterDelegate } from "../meny/hantera-fastigheter/hantera-fastigheter.component";
import { FastighetService } from "../../../services/fastighet.service";
import { AvtalService } from "../../../services/avtal.service";
import Layer from "ol/layer/Layer";

export class MkHanteraFastigheterDelegateImpl implements MkHanteraFastigheterDelegate {

    private mapComponent: MkMapComponent;
    private projektService: ProjektService;
    private fastighetService: FastighetService;
    private avtalService: AvtalService;
    private projektId: uuid;
    private fastighetOnClick = new Subject<uuid>();
    private clickSubscription: Subscription;

    private get hanteraFastigheterLayer(): Layer {
        return this.mapComponent.getLayer("hanteraFastigheterLayer");
    }

    private get avtalsstatusLayer(): Layer {
        return this.mapComponent.getLayer("avtalsstatus");
    }

    private get fastighetsytorLayer(): Layer {
        return this.mapComponent.getLayer("fastighetsytorLayer");
    }

    private get mittlinjeredovisadesamfLayer(): Layer {
        return this.mapComponent.getLayer("mittlinjeredovisadesamfLayer");
    }

    constructor(mapComponent: MkMapComponent,
        projektService: ProjektService,
        projektId: uuid,
        fastighetService: FastighetService,
        avtalService: AvtalService) {
        this.mapComponent = mapComponent;
        this.projektService = projektService;
        this.projektId = projektId;
        this.fastighetService = fastighetService;
        this.avtalService = avtalService;
    }
    get onActivate$(): Observable<void> {
        throw new Error("Method not implemented.");
    }
    get onDeactivate$(): Observable<void> {
        throw new Error("Method not implemented.");
    }
    get verktygActivated$(): Observable<void> {
        throw new Error("Method not implemented.");
    }

    activateMenyval(): void {
        this.avtalsstatusLayer.setVisible(false);

        this.mittlinjeredovisadesamfLayer.setVisible(true);
        this.fastighetsytorLayer.setVisible(true);
        this.hanteraFastigheterLayer.setVisible(true);

        if (!this.clickSubscription) {
            this.clickSubscription = this.mapComponent.infoClick
                .pipe(
                    map(value => {
                        return value.filter(result => (result.layerId === "fastighetsytorLayer" || result.layerId === "mittlinjeredovisadesamfLayer"));
                    })
                )
                .subscribe(result => {
                    let res = result.find(result => result.layerId === "mittlinjeredovisadesamfLayer");
                    if (!(res?.features.length > 0)) {
                        res = result.find(result => result.layerId === "fastighetsytorLayer");
                    }
                    if (res?.features.length > 0) {
                        const objektId = res?.features[0].get("objekt_id");
                        if (objektId) {
                            this.fastighetOnClick.next(objektId);
                        }
                    }
                });
        }
    }

    inactivateMenyval(): void {
        this.clickSubscription.unsubscribe();
        this.clickSubscription = null;
    }

    saveChanges(addedFastigheter: string[], removedFastigheter: string[]): Observable<void> {
        let addFastigheter = this.addFastigheter(addedFastigheter);
        let removeFastigheter = this.excludeFastigheter(removedFastigheter);

        return forkJoin([addFastigheter, removeFastigheter]).pipe(
            map(_ => this.avtalService.sendAvtalListChange$())
        );
    }

    getLayer(): VectorLayer<any> {
        return this.mapComponent.getLayer("hanteraFastigheterLayer") as VectorLayer<any>;
    }

    getDelomradenForProjekt(): Observable<FastighetDelomradeInfo[]> {
        return this.projektService.getDelomradenForProjekt(this.projektId);
    }

    getDelomradenForFastighet(fastighetId: string): Observable<FastighetDelomradeInfo[]> {
        return this.fastighetService.fetchDelomradenForFastighet(fastighetId);
    }

    getFastighetOnClick(): Observable<uuid> {
        return this.fastighetOnClick.asObservable();
    }

    private excludeFastigheter(fastighetsIds: string[]): Observable<void> {
        return this.projektService.excludeFastigheter(this.projektId, fastighetsIds);
    }

    private addFastigheter(fastighetsIds: string[]): Observable<void> {
        return this.projektService.addFastigheter(this.projektId, fastighetsIds);
    }
}
