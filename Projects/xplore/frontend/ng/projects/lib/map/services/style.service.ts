import {Injectable} from "@angular/core";
import Style from "ol/style/Style";
import Fill from "ol/style/Fill";
import CircleStyle from "ol/style/Circle";
import Stroke from "ol/style/Stroke";
import Icon from "ol/style/Icon";
import Polygon from "ol/geom/Polygon";
import {toContext} from "ol/render";
import {ConfigService} from "../../config/config.service";
import Text from "ol/style/Text";

@Injectable({
  providedIn: "root"
})
export class StyleService {

  constructor(private configService: ConfigService) {

  }

  static createStyleArray(styleDef: any[]) {
    return styleDef.map(StyleService.createStyle);
  }

  static createStyle(styleDef) {
    const style = new Style();
    if (styleDef.fill) {
      if (styleDef.fill.color) {
        style.setFill(new Fill(styleDef.fill));

      } else if (styleDef.fill.pattern){
        if (styleDef.fill.pattern.img) {
          StyleService.createImagePattern(style, styleDef.fill.pattern.img.src);
        } else if (styleDef.fill.pattern.hatching) {
          style.setFill(new Fill({color: StyleService.createHatchingPattern(styleDef.fill.pattern.hatching)}));
        }
      }
    }
    if (styleDef.stroke) {
      style.setStroke(new Stroke(styleDef.stroke));
    }
    if (styleDef.text) {
      const textStyle = new Text(styleDef.text);
      textStyle.setFill(new Fill(styleDef.text.fill));
      textStyle.setStroke(new Stroke(styleDef.text.stroke));
      style.setText(textStyle);
    }

    // Kollar först om den är en iconImage för kladdlager, annars kollar man om den är en icon för någon annan WFS ( tex restauranger)
    // crossOrigin behövs om vi ska kunna exportera en kartbild som innehåller ikoner.
    // Annars betraktas canvasen som "tainted".
    if (styleDef.image && styleDef.image.iconImage) {
      style.setImage(new Icon({src: styleDef.image.iconImage.src, color: styleDef.image.iconImage.color, crossOrigin: "anonymous"}));
    } else if (styleDef.icon) {
      // (...styleDef.icon, crossOrigin) betyder att alla attribut i icon samt attributet crossOrigin följer med.
      style.setImage(new Icon({...styleDef.icon, crossOrigin: "anonymous"}));
    } else if (styleDef.circle) {
      const stroke = new Stroke(styleDef.circle.stroke);
      const fill = new Fill(styleDef.circle.fill);
      style.setImage(new CircleStyle({stroke: stroke, fill: fill, radius: styleDef.circle.radius}))
    } else {
      // Vi sätter en default circle style så att brytpunkter ska visas om man ritar med denna stil.
      style.setImage(new CircleStyle({
        fill: style.getFill(),
        stroke: style.getStroke(),
        radius: 5
      }));
    }
    return style;
  }

  static createStyleDef(style: Style) {
      return StyleService.replaceStylePropertyKeys(JSON.parse(JSON.stringify(style)));
  }

  private static replaceStylePropertyKeys(object: any) {
    for (const prop in object) {
        if (object.hasOwnProperty(prop)) {
            if (typeof object[prop] === "object" && object[prop] !== null) {
                StyleService.replaceStylePropertyKeys(object[prop]);
            }
            if (prop.endsWith("_")) {
                object[prop.slice(0, -1)] = object[prop];
                delete object[prop];
            }
        }
    }
    return object;
  }

  static drawLegendOnCanvas(style, canvas: HTMLCanvasElement, size: [number, number]) {
    const context = canvas.getContext("2d");
    const vectorContext = toContext(context, {size: size});

    const styles = style[0].map(StyleService.createStyle);

    const width = size[0];
    const height = size[1];
    styles.forEach(style => {
      vectorContext.setStyle(style);
      vectorContext.drawGeometry(new Polygon(
        [[
          [width * 0.25, height * 0.05],
          [width * 0.50, height * 0.55],
          [width * 0.95, height * 0.40],
          [width * 0.85, height * 0.95],
          [width * 0.05, height * 0.80],
          [width * 0.25, height * 0.05]
        ]]));
    });
  }

  createLegend(styleKey: string, size: [number, number]): string {
    const style = this.configService.config.styles[styleKey];
    if (style) {
      if (style[0][0]["icon"]) {
        return style[0][0]["icon"].src;
      } else {
        const canvas: HTMLCanvasElement = document.createElement("canvas");
        StyleService.drawLegendOnCanvas(style, canvas, size);
        return canvas.toDataURL();
      }
    }

    return "";
  }

  private static createHatchingPattern(hatching): CanvasPattern {
    const canvas = document.createElement("canvas");
    const thickness = hatching.thickness ? hatching.thickness : 1;
    canvas.height = 10 + thickness;
    canvas.width = 10 + thickness;
    const context = canvas.getContext("2d");
    // The line drawing primitives did not give us a seamlessly
    // repeated image. Probably due to anti aliasing or something.
    context.fillStyle = hatching.color;
    switch (hatching.name) {
      case "backslash":
        for (var i = 0; i <= canvas.width - 1; i++) {
          context.fillRect(i, i, thickness, 1);
          if (i + thickness > canvas.width) {
            context.fillRect(0, i, i + thickness - canvas.width, 1);
          }
        }
        break;
      case "slash":
        for (var i = 0; i <= canvas.width - 1; i++) {
          context.fillRect(i, canvas.width - 1 - i, thickness, 1);
          if (i + thickness > canvas.width) {
            context.fillRect(0, canvas.width - 1 - i, i + thickness - canvas.width, 1);
          }
        }
        break;
      case "times":
        context.strokeStyle = hatching.color;
        context.moveTo(0,0);
        context.lineTo(canvas.width, canvas.height);
        context.moveTo(0, canvas.height);
        context.lineTo(canvas.width,0);
        context.lineWidth=thickness;
        context.stroke();
        break;
      case "vertline":
        context.strokeStyle = hatching.color;
        context.moveTo(canvas.width/2, 0);
        context.lineTo(canvas.width/2, canvas.height);
        context.lineWidth=thickness;
        context.stroke();
        break;
      case "horline":
        context.strokeStyle = hatching.color;
        context.moveTo(0, canvas.height/2);
        context.lineTo(canvas.width, canvas.height/2);
        context.lineWidth=thickness;
        context.stroke();
        break;
    }
    return context.createPattern(canvas, "repeat");
  }

  private static createImagePattern(style, src) {
    const canvas = document.createElement("canvas");
    const context = canvas.getContext("2d");
    const img = new Image();
    img.src = src;
    img.onload = function () {
      const pattern = context.createPattern(img, "repeat");
      style.setFill(new Fill({ color: pattern }));
    }
  }
}
