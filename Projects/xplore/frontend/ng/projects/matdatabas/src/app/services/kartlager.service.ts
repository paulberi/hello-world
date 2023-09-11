import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import LayerGroup from "ol/layer/Group";
import VectorImageLayer from "ol/layer/VectorImage";
import TileLayer from "ol/layer/Tile";
import TileWMS from "ol/source/TileWMS";
import VectorSource from "ol/source/Vector";
import GeoJSON from "ol/format/GeoJSON";
import {userDefinedLayerStyleMap} from "../common/components/styles";
import Feature from "ol/Feature";
import TileState from "ol/TileState";
import { ConfigService } from "../../../../lib/config/config.service";

export interface Kartlager {
  id: number;
  ordning: number;
  namn: string;
  grupp: string;
  beskrivning: string;
  visa: boolean;
  kartlagerfiler: Kartlagerfil[];
}

export interface Kartlagerfil {
  id: number;
  filnamn: string;
  fil: string;
  stil: string;
}

export interface Kartlagergrupp {
  namn: string;
  kartlager: Kartlager[];
}

export type LayerTreeNode = Kartlager | Kartlagergrupp;

@Injectable({
  providedIn: "root"
})
export class KartlagerService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Kartlager[]> {
    return this.http.get<Kartlager[]>("/api/kartlager");
  }

  get(id: number): Observable<Kartlager> {
    return this.http.get<Kartlager>("/api/kartlager/" + id);
  }

  post(kartlager: Partial<Kartlager>): Observable<Kartlager> {
    return this.http.post<Kartlager>("/api/kartlager", kartlager);
  }

  put(id: number, kartlager: Partial<Kartlager>): Observable<Kartlager> {
    return this.http.put<Kartlager>("/api/kartlager/" + id, kartlager);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>("/api/kartlager/" + id);
  }

  saveOrder(order: Kartlager[]): Observable<void> {
    const ids = order.map(k => k.id);
    return this.http.post<void>("/api/kartlager/order", ids);
  }

  getStyles(): Observable<string[]> {
    return this.http.get<string[]>("/api/kartlager/stilar");
  }

  getLayerTree(): Observable<LayerTreeNode[]> {
    return this.http.get<LayerTreeNode[]>("/api/kartlager/tree");
  }
}

function isKartlager(node: LayerTreeNode): node is Kartlager {
  return (node as Kartlager).id != null;
}

function isBaseLayer(node: LayerTreeNode) {
  return node.namn === "Grundkarta";
}

function isMatobjektLayer(node: LayerTreeNode) {
  return node.namn === "Mätobjekt";
}

function isKartlagergrupp(node: LayerTreeNode): node is Kartlagergrupp {
  return (node as Kartlagergrupp).kartlager != null;
}

const geoJSONFormat = new GeoJSON({
  featureProjection: "EPSG:5850"
});

/**
 * Constructs an openlayers layer tree given a LayerTreeNode
 */
export function toLayerList(http: HttpClient, root: LayerTreeNode[], tileGrid, matobjektLayer, configService: ConfigService): any[] {
  const layerUrl = configService.config.app.backgroundLayerUrl;
  const projectionCode = configService.config.projectionCode;
  const layerParams: any = configService.config.app.layerParameter;

  return root.map(node => {
    if (isKartlagergrupp(node)) {
      const group = new LayerGroup({
        layers: toLayerList(http, node.kartlager, tileGrid, matobjektLayer, configService)
      });
      group.set("isGroup", true);
      group.set("label", node.namn);
      return group;
    } else if (isKartlager(node)) {
      let layer;
      if (isBaseLayer(node)) {
        layer = new TileLayer({
          source: new TileWMS({
            projection: projectionCode,
            urls: [layerUrl],
            params: layerParams,
            tileGrid: tileGrid,
            tileLoadFunction: (image: any, src: string) => {
              http.get(src, {responseType: "blob"}).subscribe(resp => {
                  image.getImage().src = URL.createObjectURL(resp);
                },
                (error) => {
                  image.setState(TileState.ERROR);
                });
            }
          })
        });
      } else if (isMatobjektLayer(node)) {
        layer = matobjektLayer;
      } else {
        // We return a group here too because the layer may be composed of
        // multiple geojson files.
        layer = new LayerGroup({visible: node.visa});

        if (node.kartlagerfiler == null) {
          return layer;
        }

        for (const file of node.kartlagerfiler) {
          const vectorSource = new VectorSource({
            format: geoJSONFormat,
            loader: (extent, resolution, projection) => {
              http.get(`/api/kartlager/filer/${file.id}`)
                .subscribe(resp => {
                  vectorSource.addFeatures(<Feature[]> vectorSource.getFormat().readFeatures(resp));
                }, () => vectorSource.removeLoadedExtent(extent));
            }
          });

          const geoJsonLayer = new VectorImageLayer({
            visible: true,
            style: userDefinedLayerStyleMap.get(file.stil),
            zIndex: node.ordning,
            source: vectorSource,
          });
          layer.getLayers().push(geoJsonLayer);
        }
      }
      layer.setZIndex(node.ordning);
      layer.set("label", node.namn);
      layer.set("id", node.id);
      return layer;
    }
  });
}
