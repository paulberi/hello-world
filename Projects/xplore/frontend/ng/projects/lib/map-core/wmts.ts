import {LayerDef, WmtsSourceDef} from "../config/config.service";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import TileLayer from "ol/layer/Tile";
import TileState from "ol/TileState";
import WMTS from "ol/source/WMTS";
import {optionsFromCapabilities} from "ol/source/WMTS";
import WMTSCapabilities from "ol/format/WMTSCapabilities";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {LayerService} from "./layer.service";

@Injectable({
  providedIn: "root"
})
export class WMTSLayerFactory {
  constructor(private http: HttpClient) {}

  /**
   * Vi använder en custom-funktion för att ladda kartbilder. Som standard ser funktionen ut ungefär så här:
   *
   * image.getImage().src = src;
   *
   * Då har vi ingen möjlighet ett lägga på egna headers, t.ex. Authorization. Den här funktionen använder
   * angular's HttpClient som är konfigurerad att lägga på tokens för utvalda endpoints (se app.module.ts).
   */
  tileLoadFunction(image, src) {
    this.http.get(src, { responseType: "blob" }).subscribe(resp => {
      image.getImage().src = URL.createObjectURL(resp);
      },
      (error) => {
        image.setState(TileState.ERROR);
    });
  }

  createAsync(layerDef: LayerDef, layerService: LayerService): Observable<TileLayer<any>> {
    if (layerDef.source.type !== "wmts") {
      throw new Error("Wrong source type");
    }
    const wmtsSource = <WmtsSourceDef>layerDef.source;

    const url = wmtsSource.capabilitiesUrl;
    const parser = new WMTSCapabilities();
    return this.http.get(url, {responseType: "text"}).pipe(map(caps => {
      const result = parser.read(caps);
      const options = optionsFromCapabilities(result, wmtsSource.config);
      // Vi tillåter configen att överrida url:en från capabilities-svaret:
      if (wmtsSource.url) {
        options.urls = [wmtsSource.url];
      }

      // Detta behövs eftersom token-sändningen förutsätter relativa pather (t.ex. "/mapproxy"),
      // alternativet hade varit att lägga till alla hostar (en per miljö!) för alla appar i allowedUrls
      // i app.module.ts. Och då skulle man ändå få CORS-problem om man kör lokalt ("Failed to load resource:
      // Request header field Authorization is not allowed by Access-Control-Allow-Headers.").
      // Därför ges möjligeht att radera allt utom pathen från URL:en som tas från capabilities.
      if (wmtsSource.ignoreHost) {
        this.removeProtocolAndHost(options);
      }

      options.tileLoadFunction = this.tileLoadFunction.bind(this);

      // crossOrigin behövs om vi ska kunna exportera en kartbild vi laddat hem med standard-laddaren.
      // Annars betraktas canvasen som "tainted". Kan ev tas bort eller flyttas om vi gör
      // en custom-laddare med stöd för auth.
      options.crossOrigin = "anonymous";

      const source = new WMTS(options);

      source.on("tileloaderror", () => {
        if (layerDef.__layerProblemLoading !== true) {
          layerDef.__layerProblemLoading = true;
          layerService.updateLayer(layerDef);
        }
      });

      source.on("tileloadend", () => {
        if (layerDef.__layerProblemLoading !== false) {
          layerDef.__layerProblemLoading = false;
          layerService.updateLayer(layerDef);
        }
      });


      const tileLayer = new TileLayer({source: source});
      tileLayer.set("id", layerDef.id);
      tileLayer.set("layerDef", layerDef);
      tileLayer.setVisible(layerDef.visible);
      return tileLayer;
    }));
  }

  private removeProtocolAndHost(options) {
    options.urls = options.urls.map(url => {
      if (!url.startsWith("/")) {
        // Remove protocol
        url = url.replace(/(^\w+:|^)\/\//, "");
        // Remove host
        url = url.substring(url.indexOf("/"));
      }
      return url;
    });
  }
}
