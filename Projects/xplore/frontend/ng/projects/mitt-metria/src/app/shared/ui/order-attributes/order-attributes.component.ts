import { Component, Input, OnInit } from '@angular/core';
import { ProjectionService } from '../../utils/projection.service';

@Component({
  selector: 'mm-order-attributes',
  templateUrl: './order-attributes.component.html',
  styleUrls: ['./order-attributes.component.scss']
})
export class OrderAttributesComponent implements OnInit {
  @Input() attributes: string;

  constructor(private projectionService: ProjectionService) { }

  ngOnInit(): void {
  }

  //Finns det en bättre lösning för att visa vilka koordinater användaren fyllt i formuläret?
  convertCoordinates(coordinatesArray: number[][]): string {
    let allCoordinates = [];
    coordinatesArray.forEach(coordinates => {
      allCoordinates = [...allCoordinates || [], this.projectionService.transformProjection(coordinates, "EPSG:4326", "EPSG:3006")];
    })
    allCoordinates = [].concat.apply([], allCoordinates);
    allCoordinates = allCoordinates.map(coordinate => { return Number(Math.round(parseFloat(coordinate))) })
    let uniqueCoordinates = [];
    uniqueCoordinates = [...new Set(allCoordinates)];
    uniqueCoordinates = uniqueCoordinates.sort((n1, n2) => n1 - n2);
    if (uniqueCoordinates.length) {
      return `
      N-min: ${uniqueCoordinates[2]}
      N-Max: ${uniqueCoordinates[3]}
      E-min: ${uniqueCoordinates[0]}
      E-Max: ${uniqueCoordinates[1]}`
    } else {
      return "Kunde inte hitta koordinater"
    }
  }

  parseJSON(value: string): any {
    return JSON.parse(value);
  }

  checkType(value: any, key: string) {
    if (value) {
      switch (typeof value) {
        case "string":
          return "string";
        case "object":
          if (key === "polygon") {
            return "polygon";
          } else if (key === "rektangel") {
            return "rektangel"
          } else if (value.length) {
            return "array";
          } else {
            return "json";
          }
        default: "json";
      }
    }
  }

}
