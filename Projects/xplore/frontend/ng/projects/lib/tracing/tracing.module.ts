import {Inject, InjectionToken, ModuleWithProviders, NgModule} from "@angular/core";
import {ApmModule, ApmService} from "@elastic/apm-rum-angular";

export interface XpTracingConfig {
  serviceName: string;
  serverUrl?: string;
  ignoreTransactions?: Array<string | RegExp>;
  pageLoadTransactionName?: string;
}

const XpTracingConfigService = new InjectionToken<XpTracingConfig>("TracingConfig");

/**
 * Modul för att slå på Elastic APM tracing
 *
 * Exempel på användning i app.module.ts, i imports:
 *     XpTracingModule.forRoot({serviceName: "applikation-frontend"}),
 */
@NgModule({
  imports: [
    ApmModule
  ]
})
export class XpTracingModule {
  constructor(apmService: ApmService, @Inject(XpTracingConfigService) private config) {
    const hostname = location.hostname;

    let apmEnv: string;

    if (hostname.endsWith("-utv.metria.se")) {
      apmEnv = "utv";
    } else if (hostname.endsWith("-test.metria.se")) {
      apmEnv = "test";
    } else if (hostname.endsWith(".metria.se")) {
      apmEnv = "prod";
    }

    let serverUrl = "/xplore_apm";

    if (config.serverUrl) {
      serverUrl = config.serverUrl;
    }

    const appVersion = window["appVersion"];

    // tslint:disable-next-line:no-console
    console.info("Version", appVersion);

    if (apmEnv) {
      const apm = apmService.init({
        serviceName: config.serviceName,
        serviceVersion: appVersion,
        serverUrl: serverUrl,
        environment: apmEnv,
        ignoreTransactions: config.ignoreTransactions,
        pageLoadTransactionName: config.pageLoadTransactionName
      });
    }
  }

  static forRoot(config: XpTracingConfig): ModuleWithProviders<XpTracingModule> {
    return {
      ngModule: XpTracingModule,
      providers: [
        ApmService,
        {
          provide: XpTracingConfigService,
          useValue: config
        }
      ]
    };
  }
}
