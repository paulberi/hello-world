import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProjektRsp as Projekt } from '../../../../../generated/samrad-api';
import { ProjektAdminService } from '../../../../../generated/samrad-api/admin-api/api/projektAdmin.service';

@Injectable({
  providedIn: 'root'
})
export class SamradService {

  constructor(private projektService:ProjektAdminService) { }

  getSamrad$(id:string):Observable<Projekt[]>{
    return this.projektService.listProjekt(id);
  }

  getSamradById$(kundId: string, samradId: string): Observable<Projekt> {
    return this.projektService.getProjekt(kundId, samradId);
  }

  saveSamrad$(id:string, samrad:Projekt){
    return this.projektService.createProjekt(id, samrad);
  }

  updateSamrad$(kundId: string, samradId: string, samrad: Projekt) {
    return this.projektService.updateProjekt(samradId, kundId, samrad);
  }

  deleteSamrad$(kundId: string, samradId: string){
    return this.projektService.deleteProjekt(kundId, samradId);
  }
  
}