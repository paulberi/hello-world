import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

interface Asset {
  name: string;
  source: string;
}

@Component({
  selector: 'mm-product-information',
  templateUrl: './product-information.component.html',
  styleUrls: ['./product-information.component.scss']
})
export class ProductInformationComponent {
  @Input() productName: string;
  @Input() productDescription: string;
  @Input() productPreamble: string;
  @Input() productImages: Asset[] = [];
  @Input() productDocuments: Asset[] = [];
  @Input() isButtonVisibleInHeader: boolean = true;
  @Output() startOrder = new EventEmitter<void>();
  @Output() downloadDocument = new EventEmitter<any>();

  isTabletOrMobile$: Observable<boolean> = this.breakpointObserver.observe("(max-width: 991px)")
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver) { }

}
