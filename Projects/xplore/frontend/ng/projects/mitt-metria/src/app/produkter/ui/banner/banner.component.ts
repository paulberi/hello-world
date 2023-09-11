import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'mm-banner',
  templateUrl: './banner.component.html',
  styleUrls: ['./banner.component.scss']
})
export class BannerComponent {
  @Input() fullImage: string;
  @Input() tabletOrMobileImage: string;
  @Input() altText: string;

  isTabletOrMobile$: Observable<boolean> = this.breakpointObserver.observe('(max-width: 991px)')
  .pipe(
    map(result => result.matches),
    shareReplay()
  );

  constructor(private breakpointObserver: BreakpointObserver) { }

  setDefaultImage() {
    this.fullImage = "../../assets/default_image.jpeg";
    this.tabletOrMobileImage = "../../assets/default_image_medium.jpeg";
  }
}
