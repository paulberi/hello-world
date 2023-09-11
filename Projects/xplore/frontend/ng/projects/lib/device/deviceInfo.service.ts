import {Injectable, OnDestroy, Renderer2, RendererFactory2} from "@angular/core";
import {BehaviorSubject, fromEventPattern, Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";


@Injectable({
  providedIn: "root"
})
export class DeviceInfoService implements OnDestroy {

  public onResize$: BehaviorSubject<Device>;
  private _destroy$ = new Subject<void>();

  private device: Device;

  constructor(private renderFactory: RendererFactory2) {
    this.device = this.getDeviceInfo();

    this.onResize$ = new BehaviorSubject(this.device);

    const renderer = this.renderFactory.createRenderer(null, null);
    this.createOnResizeObservable(renderer);
  }

  ngOnDestroy() {
    this._destroy$.next();
    this._destroy$.complete();
  }

  private getDeviceInfo(): Device {
    return {
      screen: window.screen,
      viewport: {
        height: window.innerHeight,
        width: window.innerWidth
      },
      isMobile: this.isPhoneOrTablet()
    };
  }

  /**
   * Checks if the client device is a tablet or phone.
   *
   * Using useragent sniffing isn't the most reliable way to identify the device type,
   * but we should be able to cover the most common devices here.
   * @private
   */
  private isPhoneOrTablet(): boolean {
    // Some tablets have 'tablet' in the useragent string. Most smartphones, and a few tablets, has
    // 'mobile' in the useragent string. But to cover the most cases 'mobi' vill be used instead of mobile.
    // https://developer.mozilla.org/en-US/docs/Web/HTTP/Browser_detection_using_the_user_agent#mobile_tablet_or_desktop
    return (/tablet|mobi/i).test(navigator.userAgent);
  }

  private createOnResizeObservable(renderer: Renderer2) {
    fromEventPattern<Event>(
      () => renderer.listen("window", "resize", () => this.updateDeviceOnResize())
    ).pipe(takeUntil(this._destroy$)).subscribe();
  }

  private updateDeviceOnResize() {
      this.device = this.getDeviceInfo();

      this.onResize$.next(this.device);
  }

  isMobileDevice(): boolean {
    return this.device.isMobile;
  }

  getViewPort(): Viewport {
    return this.device.viewport;
  }

  getScreen(): Screen {
    return this.device.screen;
  }

}

export interface Device {
  screen: Screen;
  viewport: Viewport;
  isMobile: boolean;
}

export interface Viewport {
  height: number;
  width: number;
}
