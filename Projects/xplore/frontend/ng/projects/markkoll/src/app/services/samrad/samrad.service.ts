import { Injectable } from '@angular/core';
import { ProjektRsp as Projekt } from '../../../../../../generated/samrad-api';

import { SamradProjektApiService } from '../../../../../../generated/markkoll-api/api/samradProjekt.service';
import { Samrad } from '../../../../../../generated/markkoll-api';
import { KundAdminService, ProjektAdminService } from '../../../../../../generated/samrad-api/admin-api';
import { GeometriAdminService } from '../../../../../../generated/samrad-api/admin-api';
import { GeometriReq } from '../../../../../../generated/samrad-api/admin-api';


@Injectable({
  providedIn: 'root'
})
export class SamradService {

  constructor(private projektService: ProjektAdminService, private samradProjektApiService: SamradProjektApiService, private KundService:KundAdminService, private samradGeoService: GeometriAdminService) { }

  saveSamrad$(id:string, samrad:Projekt){
    return this.projektService.createProjekt(id, samrad);
  }

  getKund$(kundId:string){
    return this.KundService.getKundBySlugOrVhtNyckel(kundId);
  }

  getSamrad$(kundId: string, samradId: string) {
    return this.projektService.getProjekt(kundId, samradId);
  }

  saveSamradId$(kundId: string, markkollId: string, samradId: Samrad) {
    return this.samradProjektApiService.saveSamradiD(kundId, markkollId, samradId);
  }

  getByMarkkollId$(kundId: string, markkollId: string) {
    return this.samradProjektApiService.getByMarkkollId(kundId, markkollId);
  }

  deleteSamradProjekt(kundId: string, samradId: string) {
    return this.projektService.deleteProjekt(kundId, samradId);
  }

  updateSamradProjekt$(kundId: string, samradId: string, samradProjekt: Projekt) {
    return this.projektService.updateProjekt(kundId, samradId, samradProjekt);
  }

  saveGeometriToSamrad$(kundId: string, samradId: string, geometriReq: GeometriReq) {
    return this.samradGeoService.createGeometri(kundId, samradId, geometriReq)
  }


  listSamradFiles(kundId: string, samradId: string) {
    return this.projektService.listFilerForProjekt(kundId, samradId);
  }

  samradFileInnehall(kundId: string, projektId: string, filId: string) {
   return this.projektService.getFilForProjekt(kundId, projektId, filId);
  }
}
