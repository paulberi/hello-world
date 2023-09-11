import { Component, Input, OnChanges, OnInit, SimpleChanges } from "@angular/core";
import {
  AvtalMetadata,
  FiberVarderingsprotokoll,
} from "../../../../../../generated/markkoll-api";
import { uuid } from "../../model/uuid";
import { AvtalService } from "../../services/avtal.service";
import { MkFiberVarderingService, FiberErsattning } from "../../services/vardering-fiber.service";
import { MkFiberVarderingsprotokollService } from "../../services/varderingsprotokoll-fiber.service";

interface Identifiable {
  id?: any;
}

@Component({
  selector: "mk-varderingsprotokoll-fiber",
  templateUrl: "./varderingsprotokoll-fiber.container.html"
})
export class MkFiberVarderingsprotokollContainerComponent implements OnChanges, OnInit {

  @Input() avtalId: uuid;

  @Input() projektId: uuid;

  @Input() vp: FiberVarderingsprotokoll;

  @Input() avtalMetadata: AvtalMetadata;

  @Input() uppdragsnummer: string;

  ersattning: FiberErsattning = {
    punktersattning: [],
    markledning: [],
    intrangAkerOchSkogsmark: [],
    ovrigIntrangsersattning: [],
    summaIntrangsersattning: 0,
    grundersattning: 0,
    sarskildErsattning: 0,
    tillaggExpropriationslagen: 0,
    totalErsattning: 0
  };

  varderingService: MkFiberVarderingService = null;

  constructor(private fiberVarderingsprotokollService: MkFiberVarderingsprotokollService,
    private avtalService: AvtalService) { }

  ngOnInit() {
    this.fiberVarderingsprotokollService.getFiberVarderingConfig(this.projektId, this.vp.id)
      .subscribe(cfg => {
        this.varderingService = new MkFiberVarderingService(cfg);
        this.ersattning = this.varderingService.getErsattning(this.vp);
      });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.vp && this.varderingService) {
      this.ersattning = this.varderingService.getErsattning(changes.vp.currentValue.id);
    }
  }

  onAvtalMetadataChange(event: AvtalMetadata) {
    this.avtalService
      .setAvtalMetadata(this.avtalId, event)
      .subscribe(avtalMetadata => this.avtalMetadata = avtalMetadata);
  }

  onVpChange(vp: FiberVarderingsprotokoll) {
    this.fiberVarderingsprotokollService.updateVp(this.projektId, vp).subscribe(() => {
      this.vp = vp;
      this.ersattning = this.varderingService.getErsattning(vp);
    });
  }
}
