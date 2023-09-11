import {HttpClient} from '@angular/common/http';
import ImageLayer from "ol/layer/Image";
import TileLayer from 'ol/layer/Tile';
import ImageWMS, {Options as ImageOptions} from "ol/source/ImageWMS";
import TileWMS, {Options as TileOptions} from 'ol/source/TileWMS';
import {LoadFunction as TileLoadFunction} from 'ol/Tile';
import {LoadFunction as ImageLoadFunction} from 'ol/Image';
import TileGrid from 'ol/tilegrid/TileGrid';
import TileState from 'ol/TileState';
import {ImageTile} from 'ol';

export function createImageWms(url: string, params: any, imageLoadFunction?: ImageLoadFunction, opts?: Partial<ImageOptions>) {
    const source = new ImageWMS({
      url,
      serverType: "geoserver",
      params: { TRANSPARENT: false, ...params },
      imageLoadFunction,
      ...opts
    });
    return new ImageLayer({ source: source });
}

export function createTileWms(url: string, params: any, tileGrid: TileGrid, imageLoadFunction?: TileLoadFunction, opts?: Partial<TileOptions>) {
    const source = new TileWMS({
      urls: [url],
      serverType: "geoserver",
      params: { TRANSPARENT: false, ...params },
      tileGrid: tileGrid,
      tileLoadFunction: imageLoadFunction,
      ...opts
    });

    return new TileLayer({ source: source });
}

export function createTileGrid(extent: any, resolutions: any, tileSize: any) {
    return new TileGrid({
      extent,
      resolutions,
      tileSize
    });
}

export function imageLoadFunction(http: HttpClient): ImageLoadFunction {
  return (image, src) => {
    http.get(src, { responseType: "blob" }).subscribe(resp => {
      (image.getImage() as HTMLImageElement).src = URL.createObjectURL(resp);
    }, () => {
      // We don't do anything except avoiding propagating the error.
      // We don't need to set error state for non-tile layers.
    });
  };
}

export function tileLoadFunction(http: HttpClient): TileLoadFunction {
  return (image: ImageTile, src) => {
    http.get(src, { responseType: "blob" }).subscribe(resp => {
      (image.getImage() as HTMLImageElement).src = URL.createObjectURL(resp);
    }, () => {
      // Set error state so that the tile can be retried.
      image.setState(TileState.ERROR);
    });
  };
}
