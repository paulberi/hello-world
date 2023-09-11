import { Component, Input } from '@angular/core';

@Component({
  selector: 'mm-utcheckning-bekraftelse-ui',
  templateUrl: './utcheckning-bekraftelse-ui.component.html',
  styleUrls: ['./utcheckning-bekraftelse-ui.component.scss']
})
export class UtcheckningBekraftelseUiComponent {
  @Input() orderHistoryPath: string = "/orderhistorik"

  constructor() { }

}
