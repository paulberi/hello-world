import {toContext} from "ol/render";
import Icon from "ol/style/Icon";
import Polygon from "ol/geom/Polygon";

export function drawSampleGeometry(vectorContext, style, size: number[]) {
  const width = size[0];
  const height = size[1];

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
}

export function drawLegendOnCanvas(style, canvas: HTMLCanvasElement, size: [number, number]) {
  const context = canvas.getContext("2d");
  const vectorContext = toContext(context, {size: size});

  drawSampleGeometry(vectorContext, style, size);
}

export function createLegendUrl(style, size: [number, number]) {
  if (style.getImage() instanceof Icon) {
    return style.getImage().getSrc();
  }

  const canvas: HTMLCanvasElement = document.createElement("canvas");
  drawLegendOnCanvas(style, canvas, size);
  return canvas.toDataURL();
}

export interface HatchingPattern {
  color: string;
  thickness?: number;
  name: "backslash" | "slash" | "times" | "vertline" | "horline";
}

export function createHatchingPattern(hatching: HatchingPattern): CanvasPattern {
  const canvas = document.createElement("canvas");
  canvas.height = 10;
  canvas.width = 10;
  const context = canvas.getContext("2d");
  // The line drawing primitives did not give us a seamlessly
  // repeated image. Probably due to anti aliasing or something.
  // That's why we're drawing "pixel by pixel".
  context.fillStyle = hatching.color;
  const thickness = hatching.thickness ? hatching.thickness : 1;
  switch (hatching.name) {
    case "backslash":
      for (let i = 0; i <= 9; i++) {
        context.fillRect(i, i, thickness, thickness);
      }
      break;
    case "slash":
      for (let i = 0; i <= 9; i++) {
        context.fillRect(i, 9 - i, thickness, thickness);
      }
      break;
    case "times":
      for (let i = 0; i <= 9; i++) {
        context.fillRect(i, i, thickness, thickness);
        context.fillRect(i, 9 - i, thickness, thickness);
      }
      break;
    case "vertline":
      for (let i = 0; i <= 9; i++) {
        context.fillRect(5, i, thickness, thickness);
      }
      break;
    case "horline":
      for (let i = 0; i <= 9; i++) {
        context.fillRect(i, 5, thickness, thickness);
      }
      break;
  }
  return context.createPattern(canvas, "repeat");
}
