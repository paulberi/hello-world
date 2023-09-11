import {NgModule} from "@angular/core";
import {LayerPanelComponent} from "./components/layer-panel/layer-panel.component";
import {CommonModule, Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";
import {MatButtonModule} from "@angular/material/button";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDividerModule} from "@angular/material/divider";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatMenuModule} from "@angular/material/menu";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatRadioModule} from "@angular/material/radio";
import {MatSelectModule} from "@angular/material/select";
import {MatSliderModule} from "@angular/material/slider";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatTooltipModule} from "@angular/material/tooltip";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LayerPanelLayerComponent} from "./components/layer-panel/layer-panel-layer.component";
import {LayerPanelGroupComponent} from "./components/layer-panel/layer-panel-group.component";
import {LayerPanelLegendComponent} from "./components/layer-panel/layer-panel-legend.component";
import {BackgroundButtonsPanelComponent} from "./components/background-buttons-panel/background-buttons-panel.component";
import {BackgroundButtonComponent} from "./components/background-buttons-panel/background-button.component";
import {AdressPanelComponent} from "./components/featureinfo/adress-panel.component";
import {FsokPanelComponent} from "./components/featureinfo/fastighet/fsokpanel/fsok-panel.component";
import {CoordinatePanelComponent} from "./components/featureinfo/coordinate-panel.component";
import {FeatureInfoPanelComponent} from "./components/featureinfo/feature-info-panel.component";
import {HttpClientModule} from "@angular/common/http";
import {ToolbarComponent} from "./components/toolbar/toolbar.component";
import {PopoverComponent} from "./components/popover/popover.component";
import {CollapseButtonComponent} from "./components/collapsable-panel/collapse-button.component";
import {CollapsablePanelComponent} from "./components/collapsable-panel/collapsable-panel.component";
import {AttributionComponent} from "./components/attribution/attribution.component";
import {DraggablePanelComponent} from "./components/draggable-panel/draggable-panel.component";
import {ToolbarCollapsibleComponent} from "./components/toolbar/toolbar-collapsible.component";
import {ToolSettingsComponent} from "./components/toolbar/tool-settings.component";
import {NavigationToolbarComponent} from "./components/toolbar/navigation-toolbar.component";
import {FastighetsnamnPanelComponent} from "./components/featureinfo/fastighet/fastighetsnamn-panel/fastighetsnamn-panel.component";
import {MeasureToolbarComponent} from "./components/toolbar/sub-toolbars/measure-toolbar.component";
import {BaseToolbarComponent} from "./components/toolbar/sub-toolbars/base-toolbar.component";
import {FeatureInfoEditPanelComponent} from "./components/featureinfo/feature-info-edit-panel/feature-info-edit-panel.component";
import {FeatureInfoReadPanelComponent} from "./components/featureinfo/feature-info-read-panel.component";
import {MenuComponent} from "./components/menu/menu.component";
import {SearchPanelComponent} from "./components/search-panel/search-panel.component";
import {SearchTypeComponent} from "./components/search-panel/search-type.component";
import {SearchFieldComponent} from "./components/search-panel/search-field.component";
import {SearchOrtComponent} from "./components/search-panel/types/search-ort.component";
import {SearchAddressComponent} from "./components/search-panel/types/search-address.component";
import {SearchFastighetComponent} from "./components/search-panel/types/search-fastighet.component";
import {SearchKommunComponent} from "./components/search-panel/types/search-kommun.component";
import {SearchKoordinatComponent} from "./components/search-panel/types/search-koordinat.component";
import {SearchRattighetComponent} from "./components/search-panel/types/search-rattighet.component";
import {SearchGaComponent} from "./components/search-panel/types/search-ga.component";
import {GeometriSokPanelComponent} from "./components/featureinfo/geometri-sok-panel.component";
import {SearchPlanComponent} from "./components/search-panel/types/search-plan.component";
import {ToolSettingsSheetComponent} from "./components/toolbar/tool-settings-sheet.component";
import {ExportUiComponent} from "./export/export-ui.component";
import {ExportPaperMaskComponent} from "./export/export-paper-mask.component";
import {MapCoreModule} from "../map-core/map-core.module";
import {PlaceInfoPanelComponent} from "./components/featureinfo/place-info-panel.component";
import {SecureLoadImagePipe} from "../util/secureLoadImage.pipe";
import {MatTabsModule} from "@angular/material/tabs";
import {LayerPanelMapKeyComponent} from "./components/layer-panel/layer-panel-map-key.component";
import {OverlayModule} from "@angular/cdk/overlay";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        MapCoreModule,
        MatButtonModule,
        MatCheckboxModule,
        MatRadioModule,
        MatIconModule,
        MatDividerModule,
        MatSliderModule,
        MatAutocompleteModule,
        MatFormFieldModule,
        MatInputModule,
        MatTooltipModule,
        MatMenuModule,
        MatButtonToggleModule,
        MatExpansionModule,
        MatProgressBarModule,
        MatSelectModule,
        MatProgressSpinnerModule,
        MatBottomSheetModule,
        MatSnackBarModule,
        MatTabsModule,
        OverlayModule
    ],
    declarations: [
        LayerPanelComponent,
        LayerPanelGroupComponent,
        LayerPanelLayerComponent,
        LayerPanelMapKeyComponent,
        LayerPanelLegendComponent,
        BackgroundButtonsPanelComponent,
        BackgroundButtonComponent,
        AdressPanelComponent,
        FsokPanelComponent,
        FastighetsnamnPanelComponent,
        CoordinatePanelComponent,
        FeatureInfoPanelComponent,
        FeatureInfoReadPanelComponent,
        FeatureInfoEditPanelComponent,
        PlaceInfoPanelComponent,
        ToolbarComponent,
        ToolbarCollapsibleComponent,
        ToolSettingsComponent,
        ToolSettingsSheetComponent,
        BaseToolbarComponent,
        MeasureToolbarComponent,
        PopoverComponent,
        CollapseButtonComponent,
        CollapsablePanelComponent,
        AttributionComponent,
        DraggablePanelComponent,
        NavigationToolbarComponent,
        MenuComponent,
        SearchPanelComponent,
        SearchTypeComponent,
        SearchFieldComponent,
        SearchOrtComponent,
        SearchAddressComponent,
        SearchFastighetComponent,
        SearchKommunComponent,
        SearchKoordinatComponent,
        SearchRattighetComponent,
        SearchGaComponent,
        GeometriSokPanelComponent,
        SearchPlanComponent,
        ExportUiComponent,
        ExportPaperMaskComponent,
        SecureLoadImagePipe
    ],
    exports: [
        MapCoreModule,
        LayerPanelComponent,
        BackgroundButtonsPanelComponent,
        AdressPanelComponent,
        FsokPanelComponent,
        FastighetsnamnPanelComponent,
        CoordinatePanelComponent,
        FeatureInfoPanelComponent,
        FeatureInfoReadPanelComponent,
        FeatureInfoEditPanelComponent,
        ToolbarComponent,
        ToolbarCollapsibleComponent,
        ToolSettingsComponent,
        ToolSettingsSheetComponent,
        BaseToolbarComponent,
        MeasureToolbarComponent,
        PopoverComponent,
        CollapseButtonComponent,
        CollapsablePanelComponent,
        AttributionComponent,
        DraggablePanelComponent,
        NavigationToolbarComponent,
        MenuComponent,
        SearchPanelComponent,
        SearchTypeComponent,
        SearchOrtComponent,
        SearchAddressComponent,
        SearchFastighetComponent,
        SearchKommunComponent,
        SearchKoordinatComponent,
        SearchRattighetComponent,
        SearchGaComponent,
        GeometriSokPanelComponent,
        SearchPlanComponent,
        ExportUiComponent,
        PlaceInfoPanelComponent
    ],
    providers: [
        [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }]
    ]
})
export class MapModule {
}
