import {ComponentFactoryResolver, ComponentRef, Injectable, Injector, ViewContainerRef} from "@angular/core";
import {ExportUiComponent} from "./export-ui.component";
import {ExportRequest} from "./export.service";

/**
 * This service is responsible for dynamically instantiating the map export UI.
 */
@Injectable({
  providedIn: "root"
})
export class ExportUiService {

  constructor(private resolver: ComponentFactoryResolver) {
  }

  /**
   * Add the export UI component to the passed view container. DOM wise this view should ideally be positioned
   * as a direct child of the openlayers map. This allows the export UI to span the entire map, regardless if map spans
   * the entire page or not.
   */
  open(container: ViewContainerRef, appName: string, exportButtonText: string,
       staticOrientation?: "portrait" | "landscape",
       exportCallback?: (exportRequest: ExportRequest) => Promise<void>) {
    return new Promise<void>(resolve => {
      container.clear();
      const factory = this.resolver.resolveComponentFactory(ExportUiComponent);
      const ref = container.createComponent(factory);
      ref.instance.appName = appName;
      ref.instance.exportButtonText = exportButtonText ? exportButtonText : "Skapa";
      ref.instance.exportCallback = exportCallback;
      ref.instance.staticOrientation = staticOrientation;
      ref.instance.cancelClick.subscribe(() => {
        ref.destroy();
        resolve();
      });
    });

  }
}
