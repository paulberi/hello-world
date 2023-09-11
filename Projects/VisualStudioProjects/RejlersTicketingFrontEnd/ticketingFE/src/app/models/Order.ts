import { TeardownLogic } from "rxjs";

export class Order{
  orderId:number;
  name:string;
  address:string;
  town:string;
  telefon:string;
  email:string;
  chargingBox:string;
  loadBalancing:string;
  fuse:string;
  cableLength:string;
  status:string;
  dateOfOrder:Date;
  userId:string[];
  commentId:string[];
}
