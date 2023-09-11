import { EventEmitter, Injectable, Output } from "@angular/core";
import { AbstractControlOptions, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { AvtalMetadata, FiberVarderingsprotokoll } from "../../../../../../generated/markkoll-api";
import { FiberErsattning } from "../../services/vardering-fiber.service";

@Injectable()
export class MkFiberVarderingsprotokollPresenter {
  @Output() avtalMetadataChange = new EventEmitter<AvtalMetadata>();
  @Output() vpChange = new EventEmitter<FiberVarderingsprotokoll>();

  form: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder) { }

  initializeForm(vp: FiberVarderingsprotokoll,
    ersattning: FiberErsattning,
    avtalMetadata: AvtalMetadata,
    uppdragsnummer: string) {

    const options: AbstractControlOptions = {
      updateOn: "blur"
    };

    this.form = this.fb.group({
      vpForm: this.getVpForm(vp, ersattning, uppdragsnummer),
      avtalMetadataForm: this.getAvtalMetadata(avtalMetadata),
    }, options);
  }

  private getAvtalMetadata(avtalMetadata: AvtalMetadata) {
    const avtalMetadataForm = this.fb.group({
      markslag: avtalMetadata?.markslag,
      matandeStation: avtalMetadata?.matandeStation,
      stationsnamn: avtalMetadata?.stationsnamn,
      franStation: avtalMetadata?.franStation,
      tillStation: avtalMetadata?.tillStation
    });

    avtalMetadataForm.valueChanges.subscribe(_ => {
      // Vi behöver använda getRawValue för att disablade fält inte emittas från valueChanges
      this.avtalMetadataChange.emit(avtalMetadataForm.getRawValue());
    });

    return avtalMetadataForm;
  }

  private getIntrangAkerOchSkogsmark(vp: FiberVarderingsprotokoll, ersattning: FiberErsattning) {
    return vp?.intrangAkerOchSkogsmark.map((ias, i) =>
      this.fb.group({
        beskrivning: ias.beskrivning,
        ersattning: ersattning?.intrangAkerOchSkogsmark[i]
      })
    );
  }

  private getMarkledning(vp: FiberVarderingsprotokoll, ersattning: FiberErsattning) {
    return vp?.markledning.map((m, i) =>
      this.fb.group({
        beskrivning: m.beskrivning,
        langd: m.langd,
        bredd: m.bredd,
        ersattning: ersattning?.markledning[i]
      })
    );
  }

  private getOvrigIntrangsersattning(vp: FiberVarderingsprotokoll, ersattning: FiberErsattning) {
    return vp?.ovrigIntrangsersattning.map((oie, i) =>
      this.fb.group({
        beskrivning: oie.beskrivning,
        ersattning: ersattning?.ovrigIntrangsersattning[i]
      })
    );
  }

  private getPunktersattning(vp: FiberVarderingsprotokoll, ersattning: FiberErsattning) {
    return vp?.punktersattning.map((p, i) =>
      this.fb.group({
        beskrivning: p.beskrivning,
        antal: p.antal,
        typ: p.typ,
        ersattning: ersattning?.punktersattning[i]
      })
    );
  }

  private getVpForm(vp: FiberVarderingsprotokoll, ersattning: FiberErsattning, uppdragsnummer: string): UntypedFormGroup {
    const vpForm = this.fb.group({
      id: vp.id,
      config: this.fb.group({
        sarskildErsattning: vp?.config?.sarskildErsattning
      }),
      metadata: this.fb.group({
        varderingstidpunkt: vp?.metadata?.varderingstidpunkt,
        varderingsmanOchForetag: vp?.metadata?.varderingsmanOchForetag
      }),
      punktersattning: this.fb.array(this.getPunktersattning(vp, ersattning) || []),
      markledning: this.fb.array(this.getMarkledning(vp, ersattning) || []),
      intrangAkerOchSkogsmark: this.fb.array(this.getIntrangAkerOchSkogsmark(vp, ersattning) || []),
      ovrigIntrangsersattning: this.fb.array(this.getOvrigIntrangsersattning(vp, ersattning) || []),
      uppdragsnummer: [{ value: uppdragsnummer, disabled: true }],
    });

    vpForm.valueChanges.subscribe(vpf => this.vpChange.emit(vpf));

    return vpForm;
  }
}
