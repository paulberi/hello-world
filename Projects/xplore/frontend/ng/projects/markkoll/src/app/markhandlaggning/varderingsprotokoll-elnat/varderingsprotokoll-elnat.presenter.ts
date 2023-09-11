import { EventEmitter, Injectable, Output } from "@angular/core";
import {
  AbstractControlOptions,
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from "@angular/forms";
import {
  AvtalMetadata,
  ElnatVarderingsprotokoll,
} from "../../../../../../generated/markkoll-api";
import { ElnatVarderingService } from "../../services/vardering-elnat.service";

@Injectable()
export class MkElnatVarderingsprotokollPresenter {

  @Output() avtalMetadataChange = new EventEmitter<AvtalMetadata>();
  @Output() vpChange = new EventEmitter<ElnatVarderingsprotokoll>();

  form: UntypedFormGroup;

  constructor(
    private fb: UntypedFormBuilder,
    private varderingService: ElnatVarderingService
  ) { }

  initializeForm(vp: ElnatVarderingsprotokoll,
    avtalMetadata: AvtalMetadata,
    uppdragsnummer: string) {

    const options: AbstractControlOptions = {
      updateOn: "blur"
    };

    this.form = this.fb.group({
      vpForm: this.getVpForm(vp, uppdragsnummer),
      avtalMetadataForm: this.getAvtalMetadata(avtalMetadata)
    }, options);
  }

  private getVpForm(vp: ElnatVarderingsprotokoll, uppdragsnummer: string): UntypedFormGroup {
    const vpForm = this.fb.group({
      id: vp?.id,
      markledning: this.fb.array(this.getMarkledning(vp) || []),
      punktersattning: this.fb.array(this.getPunktersattning(vp) || []),
      hinderAkermark: this.fb.array(this.getHinderAkermark(vp) || []),
      ledningSkogsmark: this.fb.array(this.getLedningSkogsmark(vp) || []),
      rotnetto: [vp?.rotnetto],
      ovrigtIntrang: this.fb.array(this.getOvrigtIntrang(vp) || []),
      ssbSkogsmark: this.fb.array(this.getSkogsmarkSSB(vp) || []),
      ssbVaganlaggning: this.fb.array(this.getVaganlaggningSSB(vp) || []),
      prisomrade: this.fb.control(vp?.prisomrade),
      metadata: this.fb.group({
        ledning: [vp?.metadata?.ledning],
        varderingsmanOchForetag: [vp?.metadata?.varderingsmanOchForetag],
        varderingstidpunkt: [vp?.metadata?.varderingstidpunkt],
        koncessionslopnr: [vp?.metadata.koncessionslopnr],
        fastighetsnummer: [vp?.metadata.fastighetsnummer],
        spanningsniva: [{ value: vp?.metadata.spanningsniva, disabled: true }]
      }),
      config: this.fb.group({
        forhojdMinimumersattning: [vp?.config.forhojdMinimumersattning, { updateOn: 'change' }],
        lagspanning: [vp?.config.lagspanning, { updateOn: 'change' }],
        ingenGrundersattning: [!!vp?.config.ingenGrundersattning, { updateOn: 'change' }],
        storskogsbruksavtalet: [!!vp?.config.storskogsbruksavtalet, { updateOn: 'change' }]
      }),
      uppdragsnummer: [{ value: uppdragsnummer, disabled: true }],
    });

    vpForm.valueChanges.subscribe(_ => this.vpChange.emit(this.form.getRawValue().vpForm));

    return vpForm;
  }

  private getAvtalMetadata(avtalMetadata: AvtalMetadata) {
    const avtalMetadataForm = this.fb.group({
      //spanningsniva: [{value: avtalMetadata?.spanningsniva, disabled: true }], TODO: flytta
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

  private getLedningSkogsmark(vp: ElnatVarderingsprotokoll) {
    return vp?.ledningSkogsmark.map(ledning =>
      this.fb.group({
        beskrivning: [ledning.beskrivning],
        ersattning: [ledning.ersattning]
      })
    );
  }

  private getMarkledning(vp: ElnatVarderingsprotokoll) {
    return vp?.markledning.map(markledning =>
      this.fb.group({
        beskrivning: [markledning.beskrivning],
        langd: markledning.langd,
        bredd: [markledning.bredd],
        ersattning: Math.round(this.varderingService.getErsattningMarkledning(markledning))
      })
    );
  }

  private getPunktersattning(vp: ElnatVarderingsprotokoll) {
    return vp?.punktersattning.map((punktersattning) =>
      this.fb.group({
        beskrivning: [punktersattning.beskrivning],
        typ: [punktersattning.typ],
        antal: [punktersattning.antal, [Validators.required]],
        ersattning: [Math.round(this.varderingService.getErsattningPunktersattning(punktersattning))]
      })
    );
  }

  private getHinderAkermark(vp: ElnatVarderingsprotokoll) {
    return vp?.hinderAkermark.map(h =>
      this.fb.group({
        beskrivning: [h.beskrivning, Validators.required],
        ersattning: [h.ersattning]
      })
    );
  }

  private getSkogsmarkSSB(vp: ElnatVarderingsprotokoll) {
    return vp?.ssbSkogsmark.map(ssbSkogsmark => this.fb.group({
      beskrivning: ssbSkogsmark.beskrivning,
      langd: ssbSkogsmark.langd,
      bredd: ssbSkogsmark.bredd,
      ersattning: Math.round(this.varderingService.getErsattningSsbSkogsmark(ssbSkogsmark, vp.prisomrade))
    }));
  }

  private getOvrigtIntrang(vp: ElnatVarderingsprotokoll) {
    return vp?.ovrigtIntrang.map(oi => this.fb.group({
      beskrivning: [oi.beskrivning],
      ersattning: [oi.ersattning]
    }));
  }

  private getVaganlaggningSSB(vp: ElnatVarderingsprotokoll) {
    return vp?.ssbVaganlaggning.map(ssbVaganlaggning =>
      this.fb.group({
        beskrivning: [ssbVaganlaggning.beskrivning],
        langd: [ssbVaganlaggning.langd],
        zon: [ssbVaganlaggning.zon, Validators.required],
        ersattning: Math.round(this.varderingService.getErsattningSsbVaganlaggning(ssbVaganlaggning))
      }));
  }
}
