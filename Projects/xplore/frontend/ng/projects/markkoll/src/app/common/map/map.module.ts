import { APP_INITIALIZER, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkMapComponent } from "./map.component";
import { MapCoreModule } from "../../../../../lib/map-core/map-core.module";
import { MatButtonModule } from "@angular/material/button";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";
import { ConfigService, MapConfig } from "../../../../../lib/config/config.service";
import { appConfig, defaultMapConfig, urlConfig } from "./map.config";

export function initApp(configService: ConfigService) {
  ConfigService.setAppConfig(appConfig, null, urlConfig);
  return () => configService.setMapConfig(<MapConfig>defaultMapConfig);
}
@NgModule({
  declarations: [MkMapComponent],
  imports: [
    CommonModule,
    MapCoreModule,
    MatButtonModule,
    MatTooltipModule,
    MatIconModule,
    TranslocoModule
  ],
  exports: [MkMapComponent],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [ConfigService],
      multi: true
    },
  ],
})
export class MkMapModule { }
