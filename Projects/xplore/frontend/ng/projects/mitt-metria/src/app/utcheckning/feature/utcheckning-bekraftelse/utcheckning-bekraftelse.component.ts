import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'mm-utcheckning-bekraftelse',
  templateUrl: './utcheckning-bekraftelse.component.html',
  styleUrls: ['./utcheckning-bekraftelse.component.scss']
})
export class UtcheckningBekraftelseComponent {

  constructor(private router: Router) { }

  async back(): Promise<void> {
    this.router.navigate(["produkter"])
  }

}
