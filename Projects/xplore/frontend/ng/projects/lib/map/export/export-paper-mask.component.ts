import {Component, ElementRef, Input, ViewChild} from "@angular/core";

/**
 * The idea behind this component is to define a transparent area the size of a paper,
 * and let the rest of the space be occupied by semi-transparent divs, giving the impression of a "mask".
 *
 * It also defines a few containers for adding stuff above and below the "paper".
 */
@Component({
  selector: "xp-export-paper-mask",
  template: `
      <div #container class="container">
        <div class="column mask-area"></div>
        <div class="middle-column">
          <div class="mask-area">
          </div>
          <div class="mask-area controls">
            <ng-content select=".above-paper"></ng-content>
          </div>
          <div class="paper-area">
            <div class="mask-area"></div>
            <div class="paper-border">
              <div #paper [ngStyle]="getPaperStyle()" class="paper">
                <ng-content select=".over-paper"></ng-content>
              </div>
            </div>
            <div class="mask-area"></div>
          </div>
          <div class="mask-area controls">
            <ng-content select=".below-paper"></ng-content>
          </div>
          <div class="mask-area" style="flex-grow: 2"></div>
        </div>
        <div class="column mask-area"></div>
      </div>
  `,
  styles: [`
    .container {
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      z-index: 1;
      height: 100%;
    }

    .column {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
    }

    .middle-column {
      display: flex;
      flex-direction: column;
      flex-grow: 0;
    }

    .mask-area {
      background: rgba(255, 255, 255, 0.75);
      flex-grow: 1;
    }

    .controls {
      pointer-events: auto;
      flex-grow: 0;
    }

    .paper-area {
      display: flex;
      flex-direction: row;
    }

    .paper-border {
      border: 2px dotted #2a2a2a;
    }
  `]
})
export class ExportPaperMaskComponent {
  @Input() size: "a3" | "a4" = "a4";
  @Input() orientation: "portrait" | "landscape" = "portrait";
  /**
   * (left/right, top/bottom) margins in meters. E.g. [0.02, 0.04].
   * This is used to anticipate a margin on an actual PDF page.
   * It doesn't affect the size of the preview, but it may affect the proportions.
   */
  @Input() margins: number[] = [0, 0];

  @ViewChild("container") container: ElementRef;
  @ViewChild("paper") paper: ElementRef;

  private sizes = {"a3": [0.297, 0.420], "a4": [0.210, 0.297]};

  /**
   * Gets paper dimensions (width, height) in meters.
   */
  getPaperDimensions() {
    const size = this.sizes[this.size];
    const marginlessDimensions = this.orientation == "portrait" ? size :  [size[1], size[0]];
    return [marginlessDimensions[0] - this.margins[0], marginlessDimensions[1] - this.margins[1]];

  }

  getPaperProportions() {
    const paperDimensions = this.getPaperDimensions();
    return paperDimensions[0] / paperDimensions[1];
  }

  /**
   * Gets the paper CSS style.
   */
  getPaperStyle() {
    if (this.container == null) {
      return {};
    }

    // The available space and the paper proportions will decide
    // the size of the paper.
    const containerSize = this.container.nativeElement.getBoundingClientRect();
    const paperProportions = this.getPaperProportions();
    const size = this.optimizeHeight(containerSize, paperProportions);

    return {"width": size[0] + "px", "height": size[1] + "px"};
  }

  private optimizeHeight(containerSize: ClientRect, paperProportions: number) {
    let f = x => [paperProportions * x, x];
    let height = containerSize.height - 300;
    while (f(height)[0] > containerSize.width * 0.70) {
      height -= 1;
    }
    return f(height);
  }

  /**
   * Gets the pixel dimensions of the paper (i.e. width, height, etc.)
   */
  getBoundingClientRect(): ClientRect {
    return this.paper.nativeElement.getBoundingClientRect();
  }
}
