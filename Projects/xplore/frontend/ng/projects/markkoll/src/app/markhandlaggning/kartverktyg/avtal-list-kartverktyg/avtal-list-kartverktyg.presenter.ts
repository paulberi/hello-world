import { EventEmitter } from "@angular/core";

export enum MkMenyvalState {
    OFF = "OFF",
    HANTERA_FASTIGHETER = "HANTERA_FASTIGHETER",
    HELP = "HELP",
    INTRANG = "INTRANG",
}

export class MkAvtalListKartverktygPresenter {

    private _isPanelOpen = false;
    public get isPanelOpen() {
        return this._isPanelOpen;
    }
    public set isPanelOpen(value) {
        this.updateToolState(value, this._activeTab);
    }

    private _activeTab: string = "help";
    public get activeTab(): string {
        return this._activeTab;
    }
    public set activeTab(value: string) {
        this.updateToolState(this._isPanelOpen, value);
    }

    toolStateChange = new EventEmitter<MkMenyvalState>();

    private updateToolState(isPanelOpen: boolean, activeTab: string) {

        if (isPanelOpen === this._isPanelOpen && activeTab === this._activeTab) {
            return;
        }
        this._isPanelOpen = isPanelOpen;
        this._activeTab = activeTab;

        let toolState = MkMenyvalState.OFF;

        if (isPanelOpen && activeTab) {
            switch (activeTab) {
                case "fastigheter":
                    toolState = MkMenyvalState.HANTERA_FASTIGHETER;
                    break;
                case "intrang":
                    toolState = MkMenyvalState.INTRANG;
                    break;
                case "help":
                    toolState = MkMenyvalState.HELP;
                    break;
            }
        }

        this.toolStateChange.emit(toolState);
    }
}
