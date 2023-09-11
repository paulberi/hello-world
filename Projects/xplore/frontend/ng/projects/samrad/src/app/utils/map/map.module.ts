import { APP_INITIALIZER, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { SrMapComponent } from "./map.component";
import { MapCoreModule } from "../../../../../lib/map-core/map-core.module";
import { TranslocoModule } from "@ngneat/transloco";
import {
  ConfigService,
  MapConfig,
} from "../../../../../lib/config/config.service";
import { appConfig, defaultMapConfig, urlConfig } from "./map.config";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";

export function initApp(configService: ConfigService) {
  ConfigService.setAppConfig(appConfig, null, urlConfig);
  return () => configService.setMapConfig(<MapConfig>defaultMapConfig);
}
@NgModule({
  declarations: [SrMapComponent],
  imports: [
    CommonModule,
    MapCoreModule,
    TranslocoModule,
    MatButtonModule,
    MatTooltipModule,
    MatIconModule,
  ],
  exports: [SrMapComponent],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [ConfigService],
      multi: true,
    },
  ],
})
export class SrMapModule {}
