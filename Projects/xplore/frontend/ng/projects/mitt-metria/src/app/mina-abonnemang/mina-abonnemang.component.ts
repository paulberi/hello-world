import { Component, OnInit } from '@angular/core';
import { Channel } from '../../../../../generated/graphql/shop/shop-api-types';
import { XpNotificationService } from '../../../../lib/ui/notification/notification.service';
import { CustomerChannelsGQL } from './data-access/mina-abonnemang.shop.generated';

interface Service {
  code: string,
  displayName: string;
  tag: string;
  link: string,
  infoLink: string
}

const LINK = "https://ehandel.metria.se/start"
const INFOLINK = "https://metria.se/erbjudande/geodata-och-kartor/metria-sesverige"

@Component({
  selector: 'mm-mina-abonnemang',
  templateUrl: './mina-abonnemang.component.html',
  styleUrls: ['./mina-abonnemang.component.scss']
})
export class MinaAbonnemangComponent implements OnInit {
  services: Service[] = [];
  isLoading: boolean = true;

  constructor(private customerChannelsGQL: CustomerChannelsGQL, private notificationService: XpNotificationService,
  ) { }

  ngOnInit(): void {
    this.getServices();
  }

  getServices(): void {
    this.customerChannelsGQL.watch().valueChanges.subscribe(({ data, loading }) => {
      this.isLoading = loading;
      if (data?.customerChannels) {
        this.services = this.getHighestRankedSeSverigeProduct(data?.customerChannels as any) || [];
      }
    }, error => {
      this.isLoading = false;
      this.notificationService.error("Kunde inte hämta produkter")
      console.error("Could not get services. Message:", error?.message)
    })
  }

  getHighestRankedSeSverigeProduct(services: Channel[]): Service[] {
    if (services) {
      const pro = services.filter(service => service.code === "sesverige-pro")
      const avtal = services.filter(service => service.code === "sesverige-avtal")
      const basic = services.filter(service => service.code === "sesverige")
      return pro?.length ? [{ code: pro[0].code, displayName: "SeSverige", tag: "pro", link: LINK, infoLink: INFOLINK }]
        : avtal?.length ? [{ code: avtal[0].code, displayName: "SeSverige", tag: "avtal", link: LINK, infoLink: INFOLINK }]
          : basic?.length ? [{ code: basic[0].code, displayName: "SeSverige", tag: "", link: LINK, infoLink: INFOLINK }]
            : [];
    } else {
      return [];
    }
  }
}
