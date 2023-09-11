import { Injectable } from '@angular/core';
import { School } from '../models/school';
import { WebReqService } from './web-req.service';

@Injectable({
  providedIn: 'root'
})
export class MunicipalityService {

  constructor(private webReqService: WebReqService) { }

  getMunicipalities() {
    return this.webReqService.get('public/municipality');
  }

  updateSchool(municipalityId : string, school : School) {
    return this.webReqService.patch(`private/municipality/${municipalityId}/school/${school._id}`, school);
  }

  getSchools() {
    return this.webReqService.get('public/schools');
  }
}
