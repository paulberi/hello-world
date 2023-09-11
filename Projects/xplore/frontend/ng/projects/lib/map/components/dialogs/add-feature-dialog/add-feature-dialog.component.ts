import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {GeometryType} from "../../../../map-core/wfs.source";
import {Icon, Style} from "ol/style";
import {StyleType} from "../../../services/state.service";

@Component({
  selector: "xp-add-feature-dialog",
  templateUrl: "./add-feature-dialog.component.html",
  styleUrls: ["../dialog.shared.scss", "./add-feature-dialog.component.scss"],
})
export class AddFeatureDialogComponent {

  geometryTypes: GeometryType[] = [];
  styles: Style[] = [];

  noSelectedGeometry = false;
  noStyleSelected = false;

  selectedFeatureOptions: AddFeatureOptions = {
    geometryType: null,
    styleType: {style: null, type: null},
    textValue: null
  };

  allowInput = false;
  invalidText = false;

  constructor(public dialogRef: MatDialogRef<AddFeatureDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.geometryTypes = this.data.geometryTypes;
    this.styles = this.data.styles;
  }

  setGeometryType(event) {
    this.selectedFeatureOptions.geometryType = event.value;
    this.allowInput = this.selectedFeatureOptions.geometryType === GeometryType.Text;
    this.noSelectedGeometry = false;
  }

  setText(event) {
    this.selectedFeatureOptions.textValue = event.target.value;
    if (this.selectedFeatureOptions.geometryType === GeometryType.Text && (this.selectedFeatureOptions.textValue == null || this.selectedFeatureOptions.textValue.trim() === "")) {
      this.invalidText = true;
      return;
    }
    this.invalidText = false;
  }

  // För att lägga till nya egna ikoner (geometrityper) i kladdlagret behöver man uppdatera tre filer: wfs.source.ts, add-feature-dialog.component.ts samt layer-panel-layer.component.ts
  //
  getIcon(geometryType: GeometryType) {
    switch (geometryType) {
      case GeometryType.Point:
        return "fiber_manual_record";
      case GeometryType.Line:
        return "show_chart";
      case GeometryType.Polygon:
        return "map";
      case GeometryType.Text:
        return "text_format";
      case GeometryType.Square:
        return "stop";
      case GeometryType.MapNeedle:
        return "location_on";
      case GeometryType.ArrowRightUp:
        return "call_made";
      case GeometryType.ArrowLeftDown:
        return "call_received";
    }
  }

  getHint(geometryType: GeometryType) {
    switch (geometryType) {
      case GeometryType.Point:
        return "Punkt";
      case GeometryType.Line:
        return "Linje";
      case GeometryType.Polygon:
        return "Polygon";
      case GeometryType.Text:
        return "Text";
      case GeometryType.Square:
        return "Kvadrat";
      case GeometryType.MapNeedle:
        return "Kartnål";
      case GeometryType.ArrowRightUp:
        return "Högerpil";
      case GeometryType.ArrowLeftDown:
        return "Vänsterpil";
    }
  }

  getFillColor(theStyle: Style) {
    return theStyle.getFill() != null ? {"background-color": theStyle.getFill().getColor()} : {"background-color": ""};
  }

  getStrokeColor(theStyle: Style) {
    return theStyle.getStroke() != null ? {"background-color": theStyle.getStroke().getColor()} : {"background-color": ""};
  }

  avbrytClick() {
    this.dialogRef.close();
  }

  fortsattClick() {
    if (this.selectedFeatureOptions.geometryType == null) {
      this.noSelectedGeometry = true;
      return;
    }
    if (this.selectedFeatureOptions.styleType.style == null) {
      this.noStyleSelected = true;
      return;
    }
    this.selectedFeatureOptions.styleType.type = this.selectedFeatureOptions.geometryType;

    if (this.selectedFeatureOptions.geometryType === GeometryType.Text) {
      if (this.selectedFeatureOptions.textValue == null || this.selectedFeatureOptions.textValue.trim() === "") {
        this.invalidText = true;
        return;
      }
      this.selectedFeatureOptions.styleType.style.getText().setText(this.selectedFeatureOptions.textValue);
      this.selectedFeatureOptions.styleType.style.setStroke(null);
      this.selectedFeatureOptions.styleType.style.setFill(null);
      this.selectedFeatureOptions.styleType.style.setImage(null);
      // Konverterar till punkt så openlayers får en typ den förstår
      this.selectedFeatureOptions.geometryType = GeometryType.Point;
    } else if (isIconType(this.selectedFeatureOptions.styleType.type)) {
      const iconPath: string = "/storage/ikoner/ritlager_" + this.selectedFeatureOptions.geometryType.toString() + ".png";
      const selectedStyle = new Style({
        image: new Icon({
          color: <string | number[]> this.selectedFeatureOptions.styleType.style.getFill().getColor(),
          crossOrigin: "anonymous",
          src: iconPath,
        })
      });

      this.selectedFeatureOptions.styleType.style = selectedStyle;
      // Konverterar till punkt så openlayers får en typ den förstår
      this.selectedFeatureOptions.geometryType = GeometryType.Point;
    }
    this.dialogRef.close(this.selectedFeatureOptions);
  }
}

const isIconType = (geometryType) => {
  return geometryType === GeometryType.Square
    || geometryType === GeometryType.MapNeedle
    || geometryType === GeometryType.ArrowRightUp
    || geometryType === GeometryType.ArrowLeftDown;
};

export interface AddFeatureOptions {
  geometryType: GeometryType;
  styleType: StyleType;
  textValue: string;
}
