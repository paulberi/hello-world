import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormGroup } from '@angular/forms';
import { ProjectionService } from '../../../../../../../../shared/utils/projection.service';
import { Property } from '../../../../../../../../shared/utils/property-utils';
import { RectanglePresenter, RectangleFormControl, RectangleFormValue } from './rectangle.presenter';

@Component({
  selector: 'mm-rectangle',
  templateUrl: './rectangle.component.html',
  styleUrls: ['./rectangle.component.scss'],
  providers: [RectanglePresenter]
})
export class RectangleComponent implements OnInit {
  @Input() dynamicForm: UntypedFormGroup;
  @Input() property: Property<string>;
  formControls: RectangleFormControl[] = [];

  get rectangleForm(): UntypedFormGroup {
    return this.rectanglePresenter.rectangleForm;
  }

  constructor(
    private projectionService: ProjectionService,
    private rectanglePresenter: RectanglePresenter
  ) {
    this.formControls = [
      {
        title: "N-Min",
        id: "nMin",
        min: 6100000,
        max: 7710000
      },
      {
        title: "N-Max",
        id: "nMax",
        min: 6100000,
        max: 7710000
      },
      {
        title: "E-Min",
        id: "eMin",
        min: 210000,
        max: 950000
      },
      {
        title: "E-Max",
        id: "eMax",
        min: 210000,
        max: 950000
      }
    ]
  }

  ngOnInit(): void {
    this.rectanglePresenter.initializeForm(this.formControls);

    this.onRectangleFormChanges();
  }

  onRectangleFormChanges() {
    this.rectanglePresenter.formChanges.subscribe((value: RectangleFormValue) => {
      if (this.rectangleForm.valid) {
        this.createGeoJSON(value);
      } else {
        this.dynamicForm.get(this.property.key).setValue(null);
      }
    })
  }

  createGeoJSON(value: RectangleFormValue) {
    const coordinates = [
      [value.eMin, value.nMin],
      [value.eMin, value.nMax],
      [value.eMax, value.nMax],
      [value.eMax, value.nMin],
      [value.eMin, value.nMin]
    ]
    let transformedCoordinates = [];
    coordinates.forEach(coordinate => {
      transformedCoordinates = [...transformedCoordinates || [], this.projectionService.transformProjection(coordinate, "EPSG:3006", "EPSG:4326")];
    })
    const geojson = {
      "type": "Feature",
      "properties": {},
      "geometry": {
        "type": "Polygon",
        "coordinates": [
          transformedCoordinates
        ]
      }
    }
    this.dynamicForm.get(this.property.key).setValue(geojson);
  }
}
