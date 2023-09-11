import { Injectable } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import moment from "moment";
import { Avtalhandelse, Infobrevhandelse, ManuellFastighethandelse, Projekthandelse, ProjekthandelseTyp, ProjektLoggItem, ProjektLoggType, } from "../../../../../../../generated/markkoll-api";

const SAMTLIGA: number = null;
@Injectable()
export class MkProjektloggPresenter {
  constructor(private translate: TranslocoService) {}

  itemTitle(item: ProjektLoggItem): string {
    switch(item.projektLoggType) {
      case ProjektLoggType.PROJEKTHANDELSE:
        return `${this.projekthandelseTitle(item as Projekthandelse)}`;
      case ProjektLoggType.AVTALHANDELSE:
        return this.translate.translate("mk.dokumenttyp.MARKUPPLATELSEAVTAL");
      case ProjektLoggType.INFOBREVHANDELSE:
        return this.translate.translate("mk.dokumenttyp.OTHERS");
      case ProjektLoggType.MANUELLFASTIGHETHANDELSE:
          return this.translate.translate("mk.projektlogg.manuellFastighetTitle");
      default:
        throw new Error("Okänd projektloggstyp: " + item.projektLoggType);
    }
  }

  itemText(item: ProjektLoggItem) {
    switch (item.projektLoggType) {
      case ProjektLoggType.PROJEKTHANDELSE:
        return this.projekthandelseText(item as Projekthandelse);
      case ProjektLoggType.AVTALHANDELSE:
        return this.avtalText(item as Avtalhandelse);
      case ProjektLoggType.INFOBREVHANDELSE:
        return this.informationsbrevText(item as Infobrevhandelse);
      case ProjektLoggType.MANUELLFASTIGHETHANDELSE:
        return this.manuellFastighetText(item as ManuellFastighethandelse);
      default:
        throw new Error("Okänd projektloggstyp: " + item.projektLoggType);
    }
  }

  private projekthandelseTitle(item: Projekthandelse) {
    switch (item.projekthandelseTyp) {
      case ProjekthandelseTyp.HAMTAMARKAGARE:
        return this.translate.translate("mk.projektlogg.getMarkagare");
      case ProjekthandelseTyp.PROJEKTINFORMATIONREDIGERAD:
        return this.translate.translate("mk.projektlogg.editProjektInfo");
      case ProjekthandelseTyp.VERSIONATERSTALLD:
        return this.translate.translate("mk.projektlogg.restoredVersion");
      case ProjekthandelseTyp.VERSIONBORTTAGEN:
        return this.translate.translate("mk.projektlogg.removedVersion");
      case ProjekthandelseTyp.VERSIONIMPORTERAD:
        return this.translate.translate("mk.projektlogg.newVersion");
      default:
        throw new Error("Okänd projekthändelsetyp: " + item.projekthandelseTyp);
    }
  }

  private projekthandelseText(item: Projekthandelse) {
    switch (item.projekthandelseTyp) {
      case ProjekthandelseTyp.HAMTAMARKAGARE:
        return this.translate.translate("mk.projektlogg.didGetMarkagare", { namn: item.skapadAv });
      case ProjekthandelseTyp.PROJEKTINFORMATIONREDIGERAD:
        return this.translate.translate("mk.projektlogg.didEditProjektInfo", { namn: item.skapadAv });
      case ProjekthandelseTyp.VERSIONATERSTALLD:
        return this.translate.translate("mk.projektlogg.didRestoredVersion", { namn: item.skapadAv });
      case ProjekthandelseTyp.VERSIONBORTTAGEN:
        return this.translate.translate("mk.projektlogg.didRemoveVersion", { namn: item.skapadAv });
      case ProjekthandelseTyp.VERSIONIMPORTERAD:
        return this.translate.translate("mk.projektlogg.didNewVersion", { namn: item.skapadAv });
      default:
        throw new Error("Okänd projekthändelsetyp: " + item.projekthandelseTyp);
    }
  }

  private manuellFastighetText(item: ManuellFastighethandelse) {
    return this.translate.translate("mk.projektlogg.addedManuellFastighet",
    {
        fastighetsbeteckning: item.fastighetsbeteckning
    });
  }

  private informationsbrevText(item: Infobrevhandelse) {
    return this.translate.translate("mk.projektlogg.createdOvrigtDokument",
    {
      skapadAv: item.skapadAv,
      antalFastigheter: this.numOfFastigheter(item.antalFastigheter)
    });
  }

  private avtalText(item: Avtalhandelse) {
    return this.translate.translate("mk.projektlogg.createdMarkupplatelseavtal",
    {
      skapadAv: item.skapadAv,
      antalFastigheter: this.numOfFastigheter(item.antalFastigheter)
    });
  }

  private numOfFastigheter(num: number): string {
    if (num === SAMTLIGA) {
      return this.translate.translate("mk.projektlogg.every");
    } else {
      return num.toString();
    }
  }
}
