import { Component } from '@angular/core';
import { SharingService } from './services/sharing.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Matsedeln';

  constructor(private sharingService : SharingService) { }

}
