import {Injectable} from "@angular/core";

export interface EnvironmentConfiguration {
    fsokUrl: string;
}

@Injectable()
export class EnvironmentConfigService {

    private environmentConfig: EnvironmentConfiguration = null;

    constructor() {
    }

    public setConfig(environmentConfig: EnvironmentConfiguration) {
      this.environmentConfig = environmentConfig;
    }

    public getConfig(): EnvironmentConfiguration {
        return this.environmentConfig;
    }
}
