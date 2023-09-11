import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
import {environment} from './environments/environment';

import {appConfig, urlConfig} from './app/app.config';
import {ConfigService} from "../../lib/config/config.service";

if (environment.production) {
  enableProdMode();
}

ConfigService.setAppConfig(appConfig, null, urlConfig);

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.log(err));
