import {Injectable, Component} from "@angular/core";
import {SelectionService} from "./selection.service";
import {StateService} from "./state.service";
import {Place, SokService} from "./sok.service";
import {ConfigService} from "../../config/config.service";
import {ViewService} from "../../map-core/view.service";
import {RealEstateIdentifier} from "../../../../generated/castor-api/model/realEstateIdentifier";
import {SendToCastorData} from "../../../../generated/castor-api/model/sendToCastorData";
import { CastorSelectionItem, DefaultService } from "../../../../generated/castor-api";



@Injectable({
  providedIn: "root"
})
export class CastorIntegrationService {

  public tempRealEstateList: RealEstateIdentifier[] = [];
  public castorSelectionList: MyCastorSelectionListItem[] = [];
  public sendToCastorData: MySendToCastorData = new MySendToCastorData();

  constructor(private selectionService: SelectionService,
              private stateService: StateService,
              private configService: ConfigService,
              private viewService: ViewService,
              private sokService: SokService,
              private castorBackendService: DefaultService) {
  }

  public addToTempRealEstateList(newREI: RealEstateIdentifier){
    let find: boolean = false;
    this.tempRealEstateList.forEach((x, index)=>{
      if (x.uuid === newREI.uuid) find = true;
    });
    if (!find){this.tempRealEstateList.push(newREI);}
  }
  public removeFromTempRealEstateList(oldREI: RealEstateIdentifier){
    this.tempRealEstateList.forEach((x, index)=>{
      if (x.uuid === oldREI.uuid) this.tempRealEstateList.splice(index, 1);
    });
  }

  public cleanTempRealEstateList(){
    this.tempRealEstateList = [];
  }

  public sendToCastor(name: string){
    this.sendToCastorData.requestName = name;
    this.sendToCastorData.realEstateIdentifierList = this.tempRealEstateList;
    try{

    
    this.castorBackendService.sendToCastor(
      <SendToCastorData>{
      requestName: this.sendToCastorData.requestName, 
      realEstateIdentifierList: this.sendToCastorData.realEstateIdentifierList}

      ).subscribe(result=> {
        if(result){
          console.log("skickat");
        } else {
          console.log("något gick fel");
        }
      })
    }
    catch(e){
      console.log(e);
    }
  }

  public getCastorSelectionList(){
    this.castorBackendService.getCastorSelections().subscribe( items => {
      items.forEach(x=>{
        var tmp = new MyCastorSelectionListItem();
        tmp.setData(x.created, x.description, x.name, x.selectionList);
        this.castorSelectionList.push(tmp);
      });
    });
    return this.castorSelectionList;
  }
}

export class MySendToCastorData implements SendToCastorData {
  requestName?: string;
  realEstateIdentifierList: Array<RealEstateIdentifier>;

  constructor() {
  }

  setData(requestName: string, realEstateIdentifierList: RealEstateIdentifier[]) {
    this.requestName = requestName;
    this.realEstateIdentifierList = realEstateIdentifierList;
  }

  getData() {
    return this;
  }
}

export class MyCastorSelectionListItem implements CastorSelectionItem {
  created?: string;
  description?: string;
  name?: string;
  selectionList?: Array<RealEstateIdentifier>;

  constructor() {
  }

  setData(created: string, description: string, name: string, selectionList: RealEstateIdentifier[]) {
    this.created = created;
    this.description = description;
    this.name = name;
    this.selectionList = selectionList;
  }

  getData() {
    return this;
  }
}

export class MyRealEstateIdentifier implements RealEstateIdentifier {
  uuid?: string;
  beteckning?: string;

  constructor() {
  }

  setData(uuid: string, beteckning: string) {
    this.uuid = uuid;
    this.beteckning = beteckning;
  }

  getData() {
    return this;
  }
}

