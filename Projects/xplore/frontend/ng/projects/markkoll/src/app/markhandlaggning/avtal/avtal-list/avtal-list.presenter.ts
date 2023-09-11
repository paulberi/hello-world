import { EventEmitter, Injectable } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { AvtalsjobbProgress, AvtalsjobbStatus } from "../../../../../../../generated/markkoll-api";
import { OptionItem } from "../../../common/filter-option/filter-option.component";
import { MkAvtalPageEvent, PageTyp } from "../../../model/avtalPageEvent";
import { MkAvtalsfilter } from "../../../model/avtalsfilter";
import { FastighetsfilterSelect } from "../../../services/fastighet.service";
import {ActionTyp} from "../../../model/actions";

@Injectable()
export class MkAvtalListPresenter {
  genericPageChange = new EventEmitter<MkAvtalPageEvent>();

  constructor(private translation: TranslocoService) {}

  onGenericPageChange(page: number, size: number, filter: MkAvtalsfilter, type: PageTyp) {
    const event: MkAvtalPageEvent = {
      page: page,
      size: size,
      filter: filter,
      type: type
    };

    this.genericPageChange.emit(event);
  }

  generateJobbText(action: string, progress: AvtalsjobbProgress, numOfAvtalSelected: number): string {
    if (action === ActionTyp.STATUS) {
      return this.translation.translate("xp.common.execute");
    }

    const typ = action ? this.translation.translate("mk.generateButton." + action) : "";
    switch (progress.status) {
      case AvtalsjobbStatus.NONE:
        return this.translation.translate(
          "mk.generateButton.create", { numOfAvtal: numOfAvtalSelected, typ: typ }
        );
      case AvtalsjobbStatus.DONE:
        return this.translation.translate("mk.generateButton.download", { typ: typ });
      case AvtalsjobbStatus.ERROR:
        return this.translation.translate("mk.generateButton.error", { typ: typ });
      case AvtalsjobbStatus.INPROGRESS:
        return this.translation.translate("mk.generateButton.inProgress",
          { generated: progress.generated, total: progress.total, typ: this.capitalizeFirstLetter(typ)}
        );
      default:
        return this.translation.translate("mk.generateButton.create", { typ: typ });
    }
  }

  filterOptions(): OptionItem[] {
    const allaFastigheter = { value: null, label: "Alla fastigheter"};

    const options = Object.values(FastighetsfilterSelect).map(filterValue => (
      {
        value: filterValue.valueOf(),
        label: this.translation.translate(`mk.fastighetsfilter.${filterValue.valueOf()}`)
      }
    ));

    return [ allaFastigheter, ...options ];
  }

  private capitalizeFirstLetter(text: string) {
    return text.charAt(0).toUpperCase() + text.slice(1);
  }
}
