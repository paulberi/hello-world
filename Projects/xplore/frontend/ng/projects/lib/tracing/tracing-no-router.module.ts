import {Inject, InjectionToken, ModuleWithProviders, NgModule, NgZone} from "@angular/core";
import {APM, ApmModule} from "@elastic/apm-rum-angular";
import {ApmBase} from "@elastic/apm-rum";

export interface XpTracingNoRouterConfig {
  serviceName: string;
  serverUrl?: string;
  ignoreTransactions?: Array<string | RegExp>;
  pageLoadTransactionName?: string;
}

const XpTracingConfigService = new InjectionToken<XpTracingNoRouterConfig>("TracingConfig");

/**
 * Modul för att slå på Elastic APM tracing
 *
 * Exempel på användning i app.module.ts, i imports:
 *     XpTracingNoRouterModule.forRoot({serviceName: "applikation-frontend"}),
 */
@NgModule({
  imports: [
    ApmModule
  ]
})
export class XpTracingNoRouterModule {
  constructor(private ngZone: NgZone,
              @Inject(APM) public apm: ApmBase,
              @Inject(XpTracingConfigService) private config) {
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
      // apmService requires routing, initialize direclty as we do not have routing
      const apmInstance = this.ngZone.runOutsideAngular(() =>
        this.apm.init({
          serviceName: config.serviceName,
          serviceVersion: appVersion,
          serverUrl: serverUrl,
          environment: apmEnv,
          ignoreTransactions: config.ignoreTransactions,
          pageLoadTransactionName: config.pageLoadTransactionName
        })
      );
    }
  }

  static forRoot(config: XpTracingNoRouterConfig): ModuleWithProviders<XpTracingNoRouterModule> {
    return {
      ngModule: XpTracingNoRouterModule,
      providers: [
        {
          provide: XpTracingConfigService,
          useValue: config
        }
      ]
    };
  }
}
