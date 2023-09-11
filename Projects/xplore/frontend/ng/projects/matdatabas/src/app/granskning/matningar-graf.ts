import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4lang_sv_SE from "@amcharts/amcharts4/lang/sv_SE";
import {EJ_GRANSKAD, FEL, FELKOD_OK, GODKANT} from "../services/matobjekt.service";
import {Gransvarde} from "../services/gransvarde.service";
import {DataSerie} from "./granskning-data.service";
import {Subject} from "rxjs";
import {Bullet, ValueAxis} from "@amcharts/amcharts4/charts";
import {formatNumber} from "@angular/common";
import {escapeHtml} from "../../../../lib/util/escapeHtml.util";

/*
 * In order by make it easier to differantiate between points in a multi-line graph
 * we cycle between a list of premade bullets - circle, triangle and square.
 *
 * Each bullet also defines a property called scale which takes the "scale" property
 * of each data point and can be used to scale an individual bullet point.
 */
class BulletFactory {
  private bulletArray: am4charts.Bullet[] = [];
  private nextBulletIndex = 0;

  constructor() {
    const circleBullet = new am4charts.CircleBullet();
    circleBullet.propertyFields.scale = "scale";
    this.bulletArray.push(circleBullet);

    const triangleBullet = new am4charts.Bullet();
    triangleBullet.propertyFields.scale = "scale";
    const triangle = triangleBullet.createChild(am4core.Triangle);
    triangle.horizontalCenter = "middle";
    triangle.verticalCenter = "middle";
    triangle.width = 15;
    triangle.height = 15;
    this.bulletArray.push(triangleBullet);

    const squareBullet = new am4charts.Bullet();
    squareBullet.propertyFields.scale = "scale";
    const square = squareBullet.createChild(am4core.Rectangle);
    square.width = 10;
    square.height = 10;
    square.horizontalCenter = "middle";
    square.verticalCenter = "middle";
    this.bulletArray.push(squareBullet);
  }

  getNextBullet() {
    const nextBullet = this.bulletArray[this.nextBulletIndex];
    this.nextBulletIndex = (this.nextBulletIndex + 1) % this.bulletArray.length;
    return nextBullet.clone();
  }
}

const colorList = [
  am4core.color("#f9403b"),
  am4core.color("#f9c33c"),
  am4core.color("#bb4282"),
  am4core.color("#7a2df9"),
  am4core.color("#17f986"),
  am4core.color("#4bade9"),
  am4core.color("#0d6ba4"),
  am4core.color("#b55f1b"),
  am4core.color("#ff9000"),
];

const strokeColorSMHI = am4core.color("#009999");

// TODO: Kolla upp vilka graftyper som ska finnas och hur de ska lagras.
export enum GraphType {
  Line = 0,
  StepLine = 1,
  Unused = 2,
  Column = 3
}

/**
 * Implementerar själva grafen som används för granskning.
 *
 * Innehåller metoder för att lägga till mätdata av olika typer (och som visualiseras lite olika).
 *
 */
export class MatningarGraf {
  seriesToggled = new Subject<{visible, series}>();
  dataPointClicked = new Subject<{ series, dataPoint }>();
  valueAxisClicked = new Subject<{ axis }>();

  statusNames: { [status: number]: string } = { 0: "Ej granskad", 1: "Godkänt", 2: "Fel" };

  private chart: am4charts.XYChart;
  private dateAxis: am4charts.DateAxis;
  private axes: { [key: string]: am4charts.ValueAxis } = {};

  private bullets = new BulletFactory();

  private _reuseAxes = true;
  private _connectLines = true;

  constructor(div: string, legendMaxHeight?: number) {
    const chart = am4core.create(div, am4charts.XYChart);

    chart.language.locale = am4lang_sv_SE;
    chart.numberFormatter.numberFormat = "#.####";

    chart.legend = new am4charts.Legend();

    if (legendMaxHeight != null) {
      chart.legend.maxHeight = legendMaxHeight;
      chart.legend.scrollable = true;
      this.setupCustomLegendMarker(chart);
    }

    chart.cursor = new am4charts.XYCursor();
    chart.maskBullets = false;

    const dateAxis = chart.xAxes.push(new am4charts.DateAxis());
    dateAxis.groupData = false;
    dateAxis.tooltipDateFormat = "yyyy-MM-dd HH:mm";
    dateAxis.max = (new Date()).getTime();
    dateAxis.keepSelection = true;
    this.dateAxis = dateAxis;

    chart.scrollbarX = new am4core.Scrollbar();
    chart.scrollbarY = new am4core.Scrollbar();

    // Use a new reference because for some reason the array gets mutated.
    chart.colors.list = colorList.slice();

    this.chart = chart;
  }

  set scrollbarXEnabled(enabled) {
    this.chart.scrollbarX.disabled = !enabled;
  }

  get scrollbarXEnabled() {
    return !this.chart.scrollbarX.disabled;
  }

  set scrollbarYEnabled(enabled) {
    this.chart.scrollbarY.disabled = !enabled;
  }

  get scrollbarYEnabled() {
    return !this.chart.scrollbarY.disabled;
  }

  /**
   * Setup chart legend marker to use custom made image sprite
   * @param chart 
   */
  setupCustomLegendMarker(chart: am4charts.XYChart) {
    chart.legend.useDefaultMarker = true;
      
    let marker = chart.legend.markers.template;
    marker.disposeChildren();
    let newMarker = marker.createChild(am4core.Sprite);
    newMarker.width = 50;
    newMarker.height = 40;
    newMarker.minHeight = 30;
    newMarker.scale = 0.6;
    newMarker.verticalCenter = "top";
    newMarker.horizontalCenter = "left";
    newMarker.propertyFields.fill = "fill";
    newMarker.adapter.add("hidden", function(hidden, target) {
      if(target?.dataItem?.dataContext["dummyData"]["path"]){
        target.path = target.dataItem.dataContext["dummyData"]["path"];
      } else {
        target.path = "m 0,18 h 40 v 4 H 0 Z";
      }
      return hidden;
    });
  }

  /**
   * Control if the lines in a LineSeries are visible.
   * If false only the bullet are shown.
   *
   * (You'd think the connect-property would do this but apparently not?)
   */
  set connectLines(connect) {
    this._connectLines = connect;
    for (const series of this.chart.series) {
      if (series instanceof am4charts.LineSeries) {
        series.strokeOpacity = connect ? 1 : 0;
      }
    }
  }

  get connectLines() {
    return this._connectLines;
  }

  /**
   * With this option set series with the same unit will share the same value axis.
   */
  set reuseAxes(reuse: boolean) {
    this._reuseAxes = reuse;
    this.rebuildAxes();
  }

  get reuseAxes() {
    return this._reuseAxes;
  }
  /**
   * Add info about gränsvärden / larm for a specific mätningstyp.
   *
   * It's represented as a constant, straight line in the graph.
   */
  addGransvarde(gransvarde: Gransvarde) {
    for (const series of this.chart.series) {
      if (+series.id === gransvarde.matningstypId) {
        const yAxis = series.yAxis;

        for (const range of yAxis.axisRanges) {
          if (+range.id === gransvarde.id) {
            return;
          }
        }

        const newRange = <any>yAxis.axisRanges.create();
        newRange.value = gransvarde.gransvarde;
        newRange.grid.stroke = series.stroke;
        newRange.grid.strokeWidth = 1.5;
        newRange.grid.strokeDasharray = 4;
        newRange.grid.strokeOpacity = 1;
        newRange.label.inside = true;
        newRange.label.text = gransvarde.matobjektNamn + " " + gransvarde.larmnivaNamn;
        newRange.label.fill = newRange.grid.stroke;
        newRange.label.verticalCenter = "bottom";
        newRange.id = gransvarde.id;
      }
    }
  }

  showGransvarde(gransvarde: Gransvarde) {
    for (const series of this.chart.series) {
      if (+series.id === gransvarde.matningstypId) {
        const yAxis = series.yAxis;

        for (const range of yAxis.axisRanges) {
          if (+range.id === gransvarde.id) {
            range.show();
            return;
          }
        }
      }
    }
  }

  hideGransvarde(gransvarde: Gransvarde) {
    for (const series of this.chart.series) {
      if (+series.id === gransvarde.matningstypId) {
        const yAxis = series.yAxis;
        for (const range of yAxis.axisRanges) {
          if (+range.id === gransvarde.id) {
            range.hide();
            return;
          }
        }
      }
    }
  }

  addMatningstyp(matningDataSeries: DataSerie, showBullets = true) {
    for (const series of this.chart.series) {
      if (+series.id === matningDataSeries.matningstypId) {
        this.loadSeries(series, matningDataSeries);
        return;
      }
    }

    const opts: SeriesOpts = {
      type: matningDataSeries.graftyp,
      showBullets: showBullets
    };

    this.createSeries(matningDataSeries, matningDataSeries.matningstypId, opts);
  }

  showMatningstyp(matningDataSeries: DataSerie) {
    for (const series of this.chart.series) {
      if (+series.id === matningDataSeries.matningstypId) {
        series.show();
        break;
      }
    }
  }

  hideMatningstyp(matningDataSeries: DataSerie) {
    for (const series of this.chart.series) {
      if (+series.id === matningDataSeries.matningstypId) {
        series.hide();
        break;
      }
    }
  }

  set LegendEnabled(enabled: boolean) {
    if (enabled) {
        this.chart.legend.show();
    } else {
        this.chart.legend.hide();
    }
  }

  /**
   * Add a specified amount of days to the range of the date axis in both directions.
   */
  extendDateAxisRange(days: number) {
    const MILLISECS_TO_DAYS = 86400000;

    const maxDate = Math.max.apply(Math, this.chart.series.values.map(series => series.data.slice(-1)[0].date));
    const minDate = Math.min.apply(Math, this.chart.series.values.map(series => series.data[0].date));

    this.dateAxis.min = minDate - MILLISECS_TO_DAYS * days;
    this.dateAxis.max = maxDate + MILLISECS_TO_DAYS * days;
  }

  extendDateAxisRangeToShowToday() {
    this.dateAxis.max = (new Date()).getTime();
  }

  /**
   * Add so called reference data (for example SMHI data), which are represented in the same way as normal mätningstyper
   * but we display them in the graph separately. The value axis is placed on the right side of the graph.
   */
  addReferensdata(matningDataSeries: DataSerie) {
    const series = this.getLoadedReferensDataSeries(matningDataSeries);
    if (series != null) {
      this.loadSeries(series, matningDataSeries);
      return;
    }

    let opts: SeriesOpts = {
      type: matningDataSeries.graftyp,
      isOppositeAxis: true,
      showBullets: false,
      defaultVisible: matningDataSeries.visible,
      stroke: colorList[matningDataSeries.matningstypId % colorList.length]
    };

    // TODO: Kolla upp färgpalett för grafen.
    if (matningDataSeries.matobjektNamn.toLowerCase().startsWith("smhi")) {
      if(matningDataSeries.matningstypNamn.toLowerCase().startsWith("dygnsneder")){
        opts.yAxixValueExtraMax = 2;
      }
      opts = {...opts, stroke: strokeColorSMHI};
    }

    this.createSeries(matningDataSeries, "ref_" + matningDataSeries.matningstypId, opts);
  }

  showReferensdata(matningDataSeries: DataSerie) {
    const series = this.getLoadedReferensDataSeries(matningDataSeries);
    if (series != null) {
      series.show();
    }
  }

  hideReferensData(matningDataSeries: DataSerie) {
    const series = this.getLoadedReferensDataSeries(matningDataSeries);
    if (series != null) {
      series.hide();
    }
  }

  private getLoadedReferensDataSeries(matningDataSeries: DataSerie) {
    for (const series of this.chart.series) {
      if (series.id === "ref_" + matningDataSeries.matningstypId && isReferenceData(series)) {
        return series;
      }
    }
    return null;
  }

  private getStoredAxis(axisLabel, isOppositeAxis) {
    if (this.axes[axisLabel]) {
      const axis = this.axes[axisLabel];
      if (axis.renderer.opposite === isOppositeAxis) {
        return axis;
      }
    }
  }

  private storeAxis(axis) {
    this.axes[axis.title.text] = axis;
    axis.cursorOverStyle = am4core.MouseCursorStyle.pointer;
    axis.events.on("hit", (ev) => this.onValueAxisClicked(ev, axis));
  }

  /**
   * Tear down all the Y axes on the left side and rebuild and reattach them.
   * The purpose is to support changing the "reuseAxes" option on the fly.
   */
  private rebuildAxes() {
    this.axes = {};

    for (const s of this.chart.series) {
      if (isReferenceData(s)) {
        continue;
      }

      const currentIndex = this.chart.yAxes.indexOf(s.yAxis);
      if (currentIndex !== -1) {
        this.chart.yAxes.removeIndex(currentIndex);
      }

      let yAxis = this.getStoredAxis(s.dummyData.yAxisLabel, false);
      if (!this.reuseAxes || yAxis == null) {
        yAxis = this.chart.yAxes.push(new am4charts.ValueAxis());
        yAxis.title.text = s.dummyData.yAxisLabel;
        this.storeAxis(yAxis);
      }

      s.yAxis = yAxis;
    }
  }


  /**
   * Given a MatningDataSeries from the backend, construct an am4graph series and attach it to the graph.
   */
  private createSeries(matningDataSeries: DataSerie, id: string, opts: SeriesOpts = {}) {
    opts = {isOppositeAxis: false, type: GraphType.Line,
      showBullets: true, defaultVisible: true, minBulletDistance: 10, autoGapCount: null,
      ...opts};
    let yAxis = this.getStoredAxis(matningDataSeries.yAxisLabel, opts.isOppositeAxis);
    if (!this.reuseAxes || yAxis == null) {
      yAxis = this.chart.yAxes.push(new am4charts.ValueAxis());
      yAxis.title.text = matningDataSeries.yAxisLabel;
      yAxis.renderer.opposite = opts.isOppositeAxis;
      yAxis.disabled = !matningDataSeries.visible;
      yAxis.keepSelection = true;
      if(opts.yAxixValueExtraMax != null){
        yAxis.extraMax = opts.yAxixValueExtraMax;
      }
      this.storeAxis(yAxis);
    }

    let series;
    switch (opts.type) {
      case GraphType.Line:
        series = this.chart.series.push(new am4charts.LineSeries());
        break;
      case GraphType.StepLine:
        series = this.chart.series.push(new am4charts.StepLineSeries());
        series.minDistance = 0;
        break;
      case GraphType.Column:
        series = this.chart.series.push(new am4charts.ColumnSeries());
        series.columns.template.fill = opts.stroke;
        series.columns.template.events.on("hit", (ev) => this.onColumnClicked(ev, matningDataSeries));
        break;
      default:
        series = this.chart.series.push(new am4charts.LineSeries());
        break;
    }

    if (!opts.defaultVisible) {
      series.hide();
    }

    series.dataFields.valueY = "value";
    series.dataFields.dateX = "date";
    series.cursorTooltipEnabled = false;
    series.strokeWidth = 2;
    series.yAxis = yAxis;
    series.connect = false;
    series.autoGapCount = opts.autoGapCount;
    series.minBulletDistance = opts.minBulletDistance;

    series.events.on("hidden", (ev) => this.handleToggleSeries(ev));
    series.events.on("shown", (ev) => this.handleToggleSeries(ev));

    this.loadSeries(series, matningDataSeries);

    const name = `${matningDataSeries.matobjektNamn} - ${matningDataSeries.matningstypNamn}`;

    series.name = name;
    series.id = id;

    if (opts.stroke != null) {
      series.stroke = opts.stroke;
      series.fill = opts.stroke;
    }


    if (opts.showBullets) {
      const valueLabel = series.bullets.push(new am4charts.LabelBullet());
      valueLabel.label.text = "{felkodShort}";
      valueLabel.label.fontSize = 16;
      valueLabel.label.verticalCenter = "bottom";

      valueLabel.adapter.add("dy", (dy, target) => {
        if (target.pixelX < 0 || target.pixelX > this.chart.plotContainer.measuredWidth
          || target.pixelY < 0 || target.pixelY > this.chart.plotContainer.measuredHeight) {
          target.visible = false;
        } else {
          target.visible = true;
        }
        return dy;
      });

      const bullet = series.bullets.push(this.bullets.getNextBullet());
      bullet.propertyFields.scale = "scale";
      bullet.propertyFields.fillOpacity = "fillOpacity";

      
      const enhet = this.getEnhet(matningDataSeries);

      bullet.tooltipHTML = "Dummy så adaptern körs";

      bullet.adapter.add("tooltipHTML", (label, target: Bullet, key) => {
        if (target.dataItem == null) {
          return "";
        }

        const data: any = target.dataItem.dataContext;
        let html = "<b>";
        html = html + this.formateraVarde(data.value, data.felkod, matningDataSeries.decimaler, enhet);
        html = html + "<br/>Status: " + this.statusNames[data.status];
        html = html + "</b>";
        return html;
      });

      bullet.adapter.add("dy", (dy, target) => {
        if (target.pixelX < 0 || target.pixelX > this.chart.plotContainer.measuredWidth
          || target.pixelY < 0 || target.pixelY > this.chart.plotContainer.measuredHeight) {
            target.visible = false;
          } else {
            target.visible = true;
          }
          return dy;
        });
        
        
        bullet.events.on("hit", (ev) => this.onDataPointClicked(ev, matningDataSeries));
        this.setupBulletPath(series, bullet);
    }

    this.chart.validateData();
  }

  /**
   * Set the path to use as image-sprite in chart legend
   * @param series 
   * @param bullet 
   */
  private setupBulletPath(series, bullet) {
      if(bullet.children.getIndex(0) instanceof am4core.Circle){
        series.dummyData["path"] = "M 19.955078,7.7675781 A 11.345354,12.233009 0 0 0 8.8085938,18 H 0 v 4 H 8.8085938 A 11.345354,12.233009 0 0 0 20,32.232422 11.345354,12.233009 0 0 0 31.189453,22 H 40 V 18 H 31.191406 A 11.345354,12.233009 0 0 0 20,7.7675781 a 11.345354,12.233009 0 0 0 -0.04492,0 z";
      } else if(bullet.children.getIndex(0) instanceof am4core.Triangle){
        series.dummyData["path"] = "M 19.910156,7.5449219 13.871094,18 H 0 v 4 H 11.560547 L 5.2871094,32.861328 34.710938,32.759766 28.394531,22 H 40 V 18 H 26.046875 Z";
      } else if(bullet.children.getIndex(0) instanceof am4core.Rectangle) {
        series.dummyData["path"] = "M 8.5429688,8.2382812 V 18 H 0 v 4 h 8.5429688 v 9.761719 H 31.457031 V 22 H 40 V 18 H 31.457031 V 8.2382812 Z";
      } else {
        series.dummyData["path"] = "m 0,18 h 40 v 4 H 0 Z";
      }
  }

  /**
   * Load or reload the data in an existing am4graph series.
   */
  private loadSeries(series, matningDataSeries: DataSerie) {
    const data = [];
    matningDataSeries.data.forEach(v => {
      let felkod = null;

      if (v.felkod === FELKOD_OK) {
        if (v.status === FEL) {
          felkod = "X";
        }
      } else {
        if (v.felkod === "Fruset") {
          felkod = "Fr";
        } else if (v.felkod === "Flödar") {
          felkod = "Fl";
        } else {
          felkod = v.felkod.charAt(0);
        }
      }

      data.push({
        "id": v.id,
        "date": v.avlastDatum,
        "value": v.varde,
        "scale": v.status === EJ_GRANSKAD ? 1.5 : 1,
        "fillOpacity": v.felkod === FELKOD_OK ? 1 : 0,
        "felkod": v.felkod,
        "felkodShort": felkod,
        "status": v.status
      });
    });

    series.data = data;
    series.dummyData = matningDataSeries;
  }

  /**
   * When a series is hidden and its axis is not used by another series
   * make sure that the axis gets hidden too.
   */
  handleToggleSeries(ev) {
    this.seriesToggled.next({visible: ev.target.visible, series: ev.target.dummyData});

    const axis = ev.target.yAxis;
    let disabled = true;
    axis.series.each(series => {
      if (!series.isHiding && !series.isHidden) {
        disabled = false;
      }
    });
    axis.disabled = disabled;
  }

  onColumnClicked(ev, series) {
    const dataItem = ev.target.dataItem;
    const context = dataItem.dataContext;
    this.dataPointClicked.next({ series: series, dataPoint: context });
  }

  onDataPointClicked(ev, series) {
    const dataItem = ev.target.dataItem;
    const context = dataItem.dataContext;
    this.dataPointClicked.next({ series: series, dataPoint: context });
  }

  onValueAxisClicked(ev, axis) {
    this.valueAxisClicked.next({ axis: axis });
  }

  set maskBullets(mask: boolean) {
    this.chart.maskBullets = mask;
  }

  setValueAxisRange(axis: ValueAxis, min: number, max: number) {
    axis.min = min;
    axis.max = max;
    axis.extraMax = 0;
    axis.extraMin = 0;
  }

  /**
   * It seems reinit() is not enough to redraw bullets correctly. We need deepInvalidate().
   */
  redraw() {
    this.chart.deepInvalidate();
  }

  set yAxisWidth(width: number) {
    for (const series of this.chart.series) {
      const yAxis = series.yAxis;
      yAxis.width = width;
    }
  }

  set legendHeight(height: number) {
    this.chart.legend.markers.template.height = height;
  }

  getEnhet(serie: DataSerie) {
    if (serie.beraknadEnhet != null) {
      return serie.beraknadEnhet;
    } else {
      return serie.enhet;
    }
  }

  formateraVarde(varde: number, felkod: string, decimaler: number, enhet: string) {
    const digitsInfo = "1.0-" + decimaler;
    if (felkod == null || felkod === "Ok") {
      return formatNumber(varde, "sv-SE", digitsInfo) + "&nbsp;" + escapeHtml(enhet);
    } else {
      return "Felkod: " + escapeHtml(felkod);
    }
  }

  dispose() {
    this.chart.dispose();
  }
}

const isReferenceData = (series) => {
  return series != null && series.yAxis.renderer.opposite;
};

interface SeriesOpts {
  isOppositeAxis?: boolean;
  type?: GraphType;
  showBullets?: boolean;
  defaultVisible?: boolean;
  minBulletDistance?: number;
  autoGapCount?: number;
  stroke?: any;
  fill?: any;
  yAxixValueExtraMax?:number;
  yAxixValueExtraMin?:number;
}
