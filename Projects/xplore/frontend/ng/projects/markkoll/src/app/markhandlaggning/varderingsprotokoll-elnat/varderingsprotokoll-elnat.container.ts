import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import {
  AvtalMetadata,
  ElnatVarderingsprotokoll,
} from "../../../../../../generated/markkoll-api";
import { removeForId, replaceForId } from "../../common/array-util";
import { uuid } from "../../model/uuid";
import { AvtalService } from "../../services/avtal.service";
import { ElnatVarderingService, Summering } from "../../services/vardering-elnat.service";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";

@Component({
  selector: "mk-varderingsprotokoll",
  templateUrl: "./varderingsprotokoll-elnat.container.html"
})
export class MkElnatVarderingsprotokollContainer implements OnChanges {

  @Input() avtalId: uuid;

  @Input() projektId: uuid;

  @Input() vp: ElnatVarderingsprotokoll;

  @Input() avtalMetadata: AvtalMetadata;

  @Input() uppdragsnummer: string;

  summering: Summering = {
    totalErsattning: 0,
    grundersattning: 0,
    tillaggExpropriationslagen: 0,
    sarskildErsattning: 0,
    intrangsErsattning: 0,
  };

  constructor(private varderingsprotokollService: ElnatVarderingsprotokollService,
              private varderingService: ElnatVarderingService,
              private avtalService: AvtalService) {}

  onAvtalMetadataChange(event: AvtalMetadata) {
    this.avtalService
        .setAvtalMetadata(this.avtalId, event)
        .subscribe(avtalMetadata => this.avtalMetadata = avtalMetadata);
  }

  onBilagorChange() {
    this.varderingsprotokollService.getVarderingsprotokollWithAvtalId(this.projektId, this.avtalId)
        .subscribe(vp => {
          this.vp = vp;
          this.summering = this.varderingService.getSummering(vp);
        });
  }

  onVpChange(vp: ElnatVarderingsprotokoll) {
    this.varderingsprotokollService.updateElnatVP(this.projektId, vp.id, vp).subscribe(() => {
      this.vp = vp;
      this.summering = this.varderingService.getSummering(vp);
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.vp) {
      this.summering = this.varderingService.getSummering(changes.vp.currentValue)
    }
  }
}
