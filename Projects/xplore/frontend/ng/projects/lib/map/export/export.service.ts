import {Injectable} from "@angular/core";
import {MapService} from "../../map-core/map.service";

import Map from "ol/Map";
import View from "ol/View";
import ImageWMS from "ol/source/ImageWMS";
import TileWMS from "ol/source/TileWMS";
import {getPointResolution} from "ol/proj";
import ImageLayer from "ol/layer/Image";

import jsPDF from "jspdf";
import {saveAs} from "file-saver";
import {ExportFormat} from "./exportformat";
import * as Excel from "exceljs/dist/exceljs.min.js";
import VectorSource from "ol/source/Vector";
import VectorLayer from "ol/layer/Vector";
import Style from "ol/style/Style";
import Stroke from "ol/style/Stroke";
import {ConfigService} from "../../config/config.service";

export interface ExtendedCanvas {
  canvas: HTMLCanvasElement;
  widthMm: number;
  heightMm: number;
  metersPerPx: number;
  mapSize: [number, number];
  scale: number;
}

export interface ExportRequest {
  title?: string;
  filename?: string;
  format?: "PDF" | "PNG" | "XLSX";
  dpi?: number;
  paperSize?: string;
  orientation?: "portrait" | "landscape";
  features?: any[];
  highlightFeatures?: boolean;
  extent?: number[];
  footer?: string;
  resizeTitleIfNecessary?: boolean;
  resizeAndSplitTitleIfNecessary?: boolean;

  valuesExportFunc?(feature: any): any;
}

const highlightStyle = [
    new Style({
        stroke: new Stroke({
            color: "cyan",
            width: 4
        })
    })
];

@Injectable({
  providedIn: "root"
})
export class ExportService {

  private copyrightText = "© Metria och Lantmäteriet";
  readonly fastighetsgransInfoText = "Fastighetsgränserna är ej juridiskt bindande";

  private paperSizes = {
    "a3": {"widthMm": 420, "heightMm": 297},
    "a4": {"widthMm": 297, "heightMm": 210}
  };

  constructor(private mapService: MapService) {
    const appConfig = ConfigService.appConfig;
    if (appConfig.exportCopyrightText) {this.copyrightText = appConfig.exportCopyrightText; }
  }

  export(request: Partial<ExportRequest>, appName?: string) {
    switch (request.format) {
      case ExportFormat.PDF:
        return this.exportPdf(request, appName);
      case ExportFormat.PNG:
        return this.exportPng(request, appName);
      case ExportFormat.EXCEL:
          return this.exportXlsx(request);
      default:
        return Promise.reject();
    }
  }

  private exportPdf(request: ExportRequest, appName?: string) {
    return this.createPdf(this.mapService.map, request).then((blob) => {
      saveAs(blob, this.getFilenameWithExtension(request, appName));
    });
  }

  private exportPng(request: ExportRequest, appName?: string) {
    return this.createPng(this.mapService.map, request).then((blob) => {
      saveAs(blob, this.getFilenameWithExtension(request, appName));
    });
  }

  private exportXlsx(request: ExportRequest) {
    return this.createExcel(request).then(blob => {
      saveAs(blob, this.getFilenameWithExtension(request));
    });
  }

  private getFilenameWithExtension(request: ExportRequest, appName?: string): string {
    const filename: string = request.title ? request.title :
      appName != null ? `${appName} - utskrift` : request.filename;

    if (!filename.toLowerCase().match("\." + request.format + "$")) {
      return filename + "." + request.format.toLowerCase();
    }

    return filename;
  }
  /**
   * Creates the "shadow" map. Tiled layers are changed to single image layers
   * because it's easier to know when the map is done loading.
   *
   * @param map the openlayers map object to print from
   * @param opts options object containing: dpi value used to calculate the pixel ratio of the print map (dpi/90),a boolean value for if the features should be highlighted and a list of the features to highlight
   * @returns {ol.Map} a new openlayers map object that is a copy of the input map
   */
  private createShadowMap(map, opts) {
    const newView = new View({
      center: map.getView().getCenter(),
      projection: map.getView().getProjection()
    });

    const newMap = new Map({
      view: newView,
      pixelRatio: opts.dpi / 90,
      controls: [],
      interactions: []
    });

    if (opts.highlightFeatures && opts.features.length > 0) {
        newMap.addLayer(new VectorLayer({
            source: new VectorSource({
                features: opts.features.map(feature => {
                    feature.setStyle(highlightStyle);
                    return feature;
                })
            }),
            visible: true,
            zIndex: 9999 // Highlight-lagret ska alltid vara längst upp
        }));
    }

    map.getLayers().forEach(group => {
      // Is it a group?
      group.getLayers().forEach(layer => {
        if (layer.getVisible()) {
          const source = layer.getSource();
          if (source instanceof ImageWMS || source instanceof TileWMS) {
            newMap.getLayers().push(new ImageLayer({
              source: new ImageWMS(<any> this.createImageOptions(source)),
              opacity: layer.getOpacity(),
              zIndex: layer.getZIndex()
            }));
          } else {
              newMap.addLayer(layer);
          }
        }
      });
    });

    return newMap;
  }

  private createImageOptions(source: ImageWMS | TileWMS) {
    const options = {
      params: source.getParams()
    };

    if (source instanceof ImageWMS) {
      options["url"] = (<ImageWMS>source).getUrl();
    } else if (source instanceof TileWMS) {
      options["url"] = (<TileWMS>source).getUrls()[0];
    }

    // We are accessing some non-public properties here so things might break in the future.
    // But there doesn't seem to be any alternative.
    if ((<any>source).serverType_) {
      options["serverType"] = (<any> source).serverType_;
    }

    if ((<any> source).hidpi_ != null) {
      options["hidpi"] = (<any> source).hidpi_;
    }

    if (source instanceof ImageWMS) {
      options["imageLoadFunction"] = (<any> source).getImageLoadFunction();
    } else if (source instanceof TileWMS) {
      options["imageLoadFunction"] = source.getTileLoadFunction();
    }

    return options;
  }

  /**
   * Returns a promise for a canvas that is resolved when each source
   * has finised downloading its image.
   *
   * @param map openlayers map object
   * @returns a promise
   */
  private getCanvas(map, width: number, height: number) {
    map.getView().getResolution();

    return new Promise((canvasResolve, canvasReject) => {
      // map.once("prerender", () => console.info("Prerender"));
      // map.once("postcompose", () => console.info("postcompose"));
      // map.once("error", (e) => console.info("error", e));
      // map.once("rendercomplete", () => console.info("rendercomplete"));

      map.once("rendercomplete", function () {
        const mapCanvas = document.createElement("canvas");
        mapCanvas.width = width;
        mapCanvas.height = height;
        const mapContext = mapCanvas.getContext("2d");
        Array.prototype.forEach.call(
          document.querySelectorAll("#exportShadowMap .ol-layer canvas"),
          function (canvas) {
            if (canvas.width > 0) {
              const opacity = canvas.parentNode.style.opacity;
              mapContext.globalAlpha = opacity === "" ? 1 : Number(opacity);
              const transform = canvas.style.transform;
              // Get the transform parameters from the style's transform matrix
              const matrix = transform
                .match(/^matrix\(([^\(]*)\)$/)[1]
                .split(",")
                .map(Number);
              // Apply the transform to the export map context
              CanvasRenderingContext2D.prototype.setTransform.apply(
                mapContext,
                matrix
              );
              mapContext.drawImage(canvas, 0, 0);
            }
          }
        );

        // Reset current transformation matrix to the identity matrix
        mapContext.setTransform(1, 0, 0, 1, 0, 0);


        canvasResolve(mapCanvas);
      });

      map.renderSync();
    });

  }

  /**
   * Return a promise for a printout object that contains the canvas object and its width and height
   * in mm, based on the supplied dpi.
   *
   * @param map the openlayers map object to print from
   * @param opts
   */
  createPrintout(map, opts): Promise<ExtendedCanvas> {
    // Initially we set the pixel widths based on a DPI of 90.
    // This will automatically be scaled by open layers due to the
    // pixelRatio options used on the print map.
    const ogcDpi = 25.4 / 0.28;
    const width = Math.round(opts.widthMm * ogcDpi / 25.4);
    const height = Math.round(opts.heightMm * ogcDpi / 25.4);

    const size = map.getSize();
    const extent = opts.extent ? opts.extent : map.getView().calculateExtent(size);

    // We create a new map to create the map image with a higher resolution and
    // in a (possibly) larger format (A4).
    const newMap = this.createShadowMap(map, opts);

    newMap.setTarget("exportShadowMap");

    newMap.setSize([width, height]);

    newMap.getView().fit(extent, {size: newMap.getSize()});

    // Project meters per px to get the correct scale for printing the scale line
    const projection = newMap.getView().getProjection();
    const resolution = newMap.getView().getResolution();
    const center = newMap.getView().getCenter();
    const projectedMetersPerPx = getPointResolution(projection, resolution, center);

    return this.getCanvas(newMap, width, height).then((canvas: HTMLCanvasElement) => {
      const scale = (width * resolution) / (opts.widthMm / 1000);
      return <ExtendedCanvas>{
        canvas: canvas,
        widthMm: opts.widthMm,
        heightMm: opts.heightMm,
        metersPerPx: projectedMetersPerPx,
        mapSize: newMap.getSize(),
        scale: scale
      };
    });
  }

  createOptions(request: ExportRequest) {
    let paperSize = this.paperSizes[request.paperSize];
    if (request.orientation === "portrait") {
      // Flip the width and height if the orientation is portrait
      paperSize = {widthMm: paperSize.heightMm, heightMm: paperSize.widthMm};
    }

    const defaultOptions = {
      widthMm: 297,
      heightMm: 210,
      dpi: 150,
      paperSize: "a4",
      orientation: "landscape",
      features: [],
      highlightFeatures: false
    };

    return {...defaultOptions, ...{
      widthMm: paperSize.widthMm,
      heightMm: paperSize.heightMm,
      dpi: request.dpi,
      paperSize: request.paperSize,
      orientation: request.orientation,
      features: request.features,
      highlightFeatures: request.highlightFeatures,
      extent: request.extent
    }};
  }

  /**
   * Returns the configured margins for PDF pages in mm.
   *
   * They are not changable at this point because the code isn't fully adaptable to them.
   */
  getPdfMargins() {
    return [20, 40];
  }

  createPdf(map, request: ExportRequest) {
    const margins = this.getPdfMargins();
    const horizMargin = margins[0];
    const vertMargin = margins[1];

    const opts = this.createOptions(request);
    opts.widthMm -= horizMargin;
    opts.heightMm -= vertMargin;

    return this.createPrintout(map, opts).then(async printResult => {
      const pdf = new jsPDF(opts.orientation, undefined, opts.paperSize);
      pdf.setFontSize(18);
      const date = this.getTodayString();
      const margin = 2 * pdf.getTextWidth("...");
      const maxTitleLength = opts.widthMm + horizMargin / 2 - pdf.getTextWidth(date) - margin;
      const titleSplitted: string[] = pdf.splitTextToSize(request.title, maxTitleLength);
      let finalTitle = [""];

      if (titleSplitted.length > 1) {
        if (request.resizeTitleIfNecessary || request.resizeAndSplitTitleIfNecessary) {
          finalTitle = this.resizeAndPotentiallySplitTitle(request.title, pdf, opts, date, horizMargin, request.resizeAndSplitTitleIfNecessary);
        } else {
          const test = request.title.substring(0, titleSplitted[0].length + 1);
          finalTitle = [this.addToFinalTitleLine(test, titleSplitted[1], pdf, maxTitleLength, margin)];
        }
      } else {
        finalTitle = [request.title];
      }

      const leftSideX = horizMargin / 2;
      const rightSideX = opts.widthMm + horizMargin / 2;
      const topY = 15;
      const bottomY = printResult.heightMm + horizMargin;

      pdf.text(finalTitle, leftSideX, topY);

      pdf.setFontSize(10);
      pdf.text(date, rightSideX - pdf.getTextWidth(date), topY);
      pdf.text(request.footer, leftSideX, bottomY + 5);

      pdf.text(this.fastighetsgransInfoText, (leftSideX + rightSideX - this.fastighetsgransInfoText.length) / 2, bottomY + 5);

      const imgWidth = 13;
      const imgHeight = 10;
      const logo = await this.loadMetriaLogo();

      pdf.addImage(logo, "PNG", rightSideX - imgWidth, bottomY, imgWidth, imgHeight);

      this.decorateImageWithScaleline(printResult.canvas, printResult.metersPerPx);
      this.decorateImageWithCopyright(printResult.canvas);
      pdf.addImage(printResult.canvas, "PNG", horizMargin / 2, vertMargin / 2, printResult.widthMm, printResult.heightMm);

      return pdf.output("blob");
    });
  }

  private resizeAndPotentiallySplitTitle(title: string, pdf: jsPDF, opts, date: string, horizMargin: number, splitIfNecessary: boolean): string[] {
    let finalTitle: string[];

    pdf.setFontSize(14);
    const margin = 2 * pdf.getTextWidth("...");
    const maxTitleLength = opts.widthMm + horizMargin / 2 - pdf.getTextWidth(date) * 1.5 - margin;

    let splitTitle: string[] = pdf.splitTextToSize(title, maxTitleLength);

    if (splitTitle.length > 1 && splitIfNecessary) {
      pdf.setFontSize(10);
      const splitMargin = 2 * pdf.getTextWidth("...");
      const maxTitleLengthSplit = opts.widthMm + horizMargin / 2 - pdf.getTextWidth(date) * 2 - splitMargin;
      finalTitle = this.generateSplitTitle(title, pdf, maxTitleLengthSplit, splitMargin);
    } else if (splitTitle.length > 1) {
      const test = title.substring(0, splitTitle[0].length + 1);
      finalTitle = [this.addToFinalTitleLine(test, splitTitle[1], pdf, maxTitleLength, margin)];
    } else {
      finalTitle = [splitTitle[0]];
    }

    return finalTitle;
  }

  // jsPDF performs a word break, so lets try to add as many characters as possible from the second line
  private addToFinalTitleLine(firstLine: string, secondLine: string, pdf: jsPDF, maxTitleLength: number, margin: number): string {

    let title = firstLine;
    let charactersAdded = 0;
    while (pdf.getTextWidth(title) < (maxTitleLength - margin) && charactersAdded < secondLine.length) {
      title += secondLine.charAt(charactersAdded++);
    }
    title += "...";

    return title;
  }

  private generateSplitTitle(title: string, pdf: jsPDF, maxTitleLength: number, margin: number): string[] {
    let splitTitle: string[] = pdf.splitTextToSize(title, maxTitleLength);

    if (splitTitle.length > 2) {
      splitTitle[1] = this.addToFinalTitleLine(splitTitle[1], splitTitle[2], pdf, maxTitleLength, margin);
      return splitTitle.slice(0, 2);
    } else {
      return splitTitle;
    }
  }

  private getTodayString(): string {
    // The replace at the end is a workaround for a bug in IE11
    return new Date().toLocaleDateString("sv-SE", {year: "numeric", month: "numeric", day: "numeric"}).replace(/\u200E/g, "");
  }

  async decorateImage(canvas: HTMLCanvasElement, resolution: number, title: string) {
    this.decorateImageWithScaleline(canvas, resolution);
    this.decorateImageWithCopyright(canvas);
    this.decorateImageWithFastighetsgransInfo(canvas);
    await this.decorateImageWithLogo(canvas);

    // Add date and title
    const baseX = 10;
    const context: CanvasRenderingContext2D = canvas.getContext("2d");
    context.font = "16pt Helvetica";
    const date = this.getTodayString();
    const dateStartPos = canvas.width - context.measureText(date).width - baseX;
    this.drawTextWithOutLine(context, date, dateStartPos, 50);

    context.font = "32pt Helvetica";
    if (title) {
      const maxTitleWidth = dateStartPos - baseX;
      if (context.measureText(title).width > maxTitleWidth) {
        while (context.measureText(title + "...").width > maxTitleWidth) {
          title = title.slice(0, -1);
        }
        title += "...";
      }
      this.drawTextWithOutLine(context, title, baseX, 50);
    }

  }

  decorateImageWithScaleline(canvas, resolution) {
    const context: CanvasRenderingContext2D = canvas.getContext("2d");
    context.font = "15px Helvetica";

    const reasonableScaleLineMeters = resolution * canvas.width * 0.10; // 10% of total width;
    const scaleLineMeterValue = this.snapValue(reasonableScaleLineMeters);

    const pixelsPerMeter = 1 / resolution;

    // Draw scale line
    context.beginPath();

    // Base line
    const baseX = 10;
    const baseY = canvas.height - 30;
    const baseLength = pixelsPerMeter * scaleLineMeterValue;
    context.moveTo(baseX, baseY);
    context.lineTo(baseX + baseLength, baseY);
    // Prongs
    for (let prong = 0; prong <= 5; prong++) {
      const x = baseX + baseLength * (prong / 5);
      // The end points are slightly taller.
      const prongHeight = (prong === 0 || prong === 5) ? 10 : 5;
      context.moveTo(x, baseY);
      context.lineTo(x, baseY - prongHeight);
    }

    context.stroke();

    const text = (scaleLineMeterValue >= 2000) ?
      (scaleLineMeterValue / 1000).toString() + " km" : scaleLineMeterValue.toString() + " m";
    this.drawTextWithOutLine(context, text, baseX + baseLength / 2 - context.measureText(text).width / 2, baseY + 20);
  }

  decorateImageWithCopyright(canvas) {
    const context: CanvasRenderingContext2D = canvas.getContext("2d");
    const baseY = canvas.height - 30;
    this.drawTextWithOutLine(context, this.copyrightText, canvas.width - context.measureText(this.copyrightText).width - 10, baseY + 20);
  }

  decorateImageWithFastighetsgransInfo(canvas) {
    const context: CanvasRenderingContext2D = canvas.getContext("2d");
    const baseY = canvas.height - 30;
    context.font = "15px Helvetica";
    const textLength = context.measureText(this.fastighetsgransInfoText);
    this.drawTextWithOutLine(context, this.fastighetsgransInfoText, (canvas.width - (textLength.width)) / 2, baseY + 20);
  }

  async decorateImageWithLogo(canvas: HTMLCanvasElement) {
    const imgWidth = 65;
    const imgHeight = 50;
    const logo = await this.loadMetriaLogo();

    const context: CanvasRenderingContext2D = canvas.getContext("2d");
    const baseY = canvas.height - imgHeight - 25;

    context.drawImage(logo, canvas.width - imgWidth - 10, baseY, imgWidth, imgHeight);
  }

  loadMetriaLogo(): Promise<HTMLImageElement> {
    return new Promise<HTMLImageElement>((resolve, reject) => {
      const logo = new Image();
      logo.onload = () => resolve(logo);
      logo.onerror = reject;
      logo.src = "./assets/lib/metria_logo.png";
    });
  }

  private drawTextWithOutLine(context: CanvasRenderingContext2D, text: string, x: number, y: number) {
    context.strokeStyle = "white";
    context.lineWidth = 3;
    context.strokeText(text, x, y);
    context.lineWidth = 1;
    context.fillText(text, x, y);
  }

  private snapValue(value: number) {
    const roundDisplayValuesMeters = [10, 25, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 50000, 100000, 200000, 500000, 1000000];
    return roundDisplayValuesMeters.reduce((curr, prev) =>
      (Math.abs(curr - value) < Math.abs(prev - value)) ? curr : prev);
  }

  async createPng(map, request: ExportRequest): Promise<any> {
    const opts = this.createOptions(request);
    return this.createPrintout(map, opts).then(async (printResult: ExtendedCanvas) => {
      const canvas = printResult.canvas;
      await this.decorateImage(canvas, printResult.metersPerPx, request.title);
      return new Promise(resolve => {
        canvas.toBlob(function (blob) {
          resolve(blob);
        });
      });
    });
  }

  createExcel(request: ExportRequest): Promise<any> {
      if (request.features == null || request.valuesExportFunc == null) {
          return Promise.reject("Excel-export misslyckades, inga objekt finns i urvalet");
      }

      const workbook = new Excel.Workbook();
      const worksheet = workbook.addWorksheet("export");

      const rows = [];
      request.features.forEach((feature) => {
          rows.push(request.valuesExportFunc(feature));
      });

      if (rows.length !== 0) {
          // Vi kan plocka ut headers genom att kika på vilka properties som ett objekt har.
        worksheet.columns = Object.getOwnPropertyNames(rows[0]).map((property) => ({
          header: property,
          key: property,
          width: property.length + 5
        }));
      } else {
          return Promise.reject("Excel-export misslyckades, inga rader kunde skapas");
      }
      worksheet.addRows(rows);
      return new Promise(resolve => {
          workbook.xlsx.writeBuffer().then((buffer) => {
              resolve(new Blob([buffer]));
          });
      });
  }
}



