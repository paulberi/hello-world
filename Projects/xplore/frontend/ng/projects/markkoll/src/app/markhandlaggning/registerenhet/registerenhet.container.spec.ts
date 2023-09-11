import { ActivatedRoute } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import FileSaver from "file-saver";
import { MockService } from "ng-mocks";
import { of, throwError } from "rxjs";
import { Avtalsstatus, Geometristatus, FastighetsProjektInfo, TillvaratagandeTyp, FiberProjekt, ProjektTyp, IndataTyp, ElnatProjekt } from "../../../../../../generated/markkoll-api";
import { XpMessageSeverity } from "../../../../../lib/ui/feedback/message/message.component";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { testMarkagare, testOmbud, testOmbud2 } from "../../../test/data";
import { MkAvtal } from "../../model/avtal";
import { uuid } from "../../model/uuid";
import { AgareService } from "../../services/agare.service";
import { AvtalService } from "../../services/avtal.service";
import { DialogService } from "../../services/dialog.service";
import { DokumentService } from "../../services/dokument.service";
import { FastighetService } from "../../services/fastighet.service";
import { ProjektService } from "../../services/projekt.service";
import { MkFiberVarderingsprotokollService } from "../../services/varderingsprotokoll-fiber.service";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";
import { VersionMessage } from "../fastighet/fastighet.component";
import { MkRegisterenhetContainer } from "./registerenhet.container";
import { XpErrorService } from "../../../../../lib/error/error.service";
import { MkProjektkartaService } from "../../services/projektkarta.service";

const avtal: MkAvtal = {
  id: "avtalId",
  lagfarnaAgare: [testMarkagare()],
  tomtrattsinnehavare: [testMarkagare()],
  ombud: [testOmbud()],
  status: Avtalsstatus.AVTALJUSTERAS,
  outredd: false,
  intrang: null,
  ersattning: null,
  avtalskarta: null,
  loggbok: [],
  anteckning: null,
  geometristatus: Geometristatus.OFORANDRAD,
  skogsfastighet: true,
  tillvaratagandeTyp: TillvaratagandeTyp.EJBESLUTAT,
  egetTillvaratagande: 123,
  samfallighet: null,
  metadata: {
    markslag: null,
    matandeStation: null,
    stationsnamn: null,
    franStation: null,
    tillStation: null
  }
};

const fastighetsinfo = {
  id: "909a6a86-903e-90ec-e040-ed8f66444c3f",
  fastighetsbeteckning: "BUSKÖTORPET 1:1",
  kommunnamn: "SKELLEFTEÅ",
  intrang: [
    {
      typ: "LAGSPANNINGSLEDNING",
      subtyp: "MARKLEDNING",
      langd: 31.892597834928477,
    },
    {
      typ: "LAGSPANNINGSLEDNING",
      subtyp: "MARKLEDNING",
      langd: 2.1453188024122967,
    },
    {
      typ: "LAGSPANNINGSLEDNING",
      subtyp: "LUFTLEDNING",
      langd: 16.089658872704923,
    },
    { typ: "KABELSKAP", subtyp: "NONE", langd: 0.0 },
  ],
  agare: [],
  omraden: [
    {
      fastighetsId: "909a6a86-903e-90ec-e040-ed8f66444c3f",
      omradeNr: 1,
      omradeTyp: "FASTIGHET",
      area_factor: 0.10450677206349929,
      extent: [
        804553.60804238,
        7228652.83503239,
        804732.97903996,
        7228762.52503328,
      ],
      geometryType: "ST_MultiPolygon",
    },
    {
      fastighetsId: "909a6a86-903e-90ec-e040-ed8f66444c3f",
      omradeNr: 1,
      omradeTyp: "INTRANG",
      area_factor: 0.10450677206349929,
      extent: [
        804640.0276581019,
        7228662.892300493,
        804681.0011023578,
        7228713.075832522,
      ],
      geometryType: null,
    },
  ],
  extent: [
    804553.60804238,
    7228652.83503239,
    804732.97903996,
    7228762.52503328,
  ],
  ersattning: 0,
  anteckning: null,
  avtalsLogg: [],
  ingaendeFastigheter: [],
  skogsfastighet: null,
};

const vp = {
  id: "7bd48d15-6f95-4d23-bfab-a5819c7a4066",
  config: {
    lagspanning: true,
    storskogsbruksavtalet: false,
    ingenGrundersattning: false,
    forhojdMinimumersattning: true,
  },
  metadata: {
    ledning: "",
    koncessionslopnr: "",
    varderingstidpunkt: "2021-11-23T15:11:44.982193",
    varderingsmanOchForetag: "Test",
  },
  punktersattning: [
    {
      id: "ac3f7ec5-8fb6-4b99-a66b-d522f90e8004",
      antal: 0,
      beskrivning: "",
      typ: "SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_6X6M",
    },
    {
      id: "6b0b66dd-7260-47a2-84a6-a5892e113729",
      antal: 0,
      beskrivning: "",
      typ: "NATSTATION_JORDBRUKSIMPEDIMENT_10X10M",
    },
  ],
  markledning: [
    {
      id: "f10f8969-1c5c-40e5-9cc4-896b006ab16e",
      beskrivning: "",
      langd: 10,
      bredd: 4,
    },
    {
      id: "d83c9100-e336-429a-a6f0-2813a1eff5c0",
      beskrivning: "Testar",
      langd: 5,
      bredd: 2,
    },
    {
      id: "797a2e38-6e64-4dad-be3d-8e5010c3297f",
      beskrivning: "",
      langd: 0,
      bredd: 1,
    },
    {
      id: "5241f949-43b6-4074-b90e-83ac0497d485",
      beskrivning: "",
      langd: 10,
      bredd: 2,
    },
  ],
  ssbSkogsmark: [
    {
      id: "f36a9eb7-33b0-4260-9214-818cf8eebca6",
      beskrivning: "",
      langd: 0,
      bredd: 0,
      prisomrade: "NORRLANDS_INLAND",
    },
  ],
  ssbVaganlaggning: [
    {
      id: "7aa3c7d9-8eb9-483e-a8c7-81f8d9d4d34e",
      beskrivning: "",
      langd: 0,
      zon: "ZON_1",
    },
  ],
};

const fiberProjekt: FiberProjekt = {
  projektInfo: {
    projektTyp: ProjektTyp.FIBER,
    namn: "Projekt"
  },
  fiberInfo: {
    varderingsprotokoll: true
  },
  indataTyp: IndataTyp.DPCOM
};

const elnatProjekt: ElnatProjekt = {
  projektInfo: {
    projektTyp: ProjektTyp.FIBER,
    namn: "Projekt"
  },
  elnatInfo: {
  },
  indataTyp: IndataTyp.DPPOWER
}

const MESSAGE: VersionMessage = {
  title: "message",
  text: "text",
  severity: XpMessageSeverity.Success,
  actionLabel: "label"
};

class MkRegisterenhetContainerConcrete extends MkRegisterenhetContainer {
  onOmbudCreated() {}
}

describe(MkRegisterenhetContainer.name, () => {
  const PROJEKT_ID: uuid = "PROJEKTID";
  const PROJEKTTYP = "Fiber";
  const ERROR = "Error";
  const throwError$ = jest.fn((_projektId, _fastighetId) => throwError(ERROR));

  const ombud = testOmbud();
  const ombud2 = testOmbud2();

  let container: MkRegisterenhetContainer;
  let agareService: AgareService;
  let avtalService: AvtalService;
  let dialogService: DialogService;
  let dokumentService: DokumentService;
  let fastighetService: FastighetService;
  let notificationService: XpNotificationService;
  let translation: TranslocoService;
  let varderingsprotokollService: ElnatVarderingsprotokollService;
  let fiberVarderingsprotokollService: MkFiberVarderingsprotokollService;
  let projektService: ProjektService;
  let errorService: XpErrorService;
  let projektkartaService: MkProjektkartaService;
  const activatedRoute = MockService(ActivatedRoute, {
    snapshot: {
      params: {
        projektId: PROJEKT_ID,
        projektTyp: PROJEKTTYP

      },
    } as any
  });

  beforeEach(() => {
    jest.spyOn(FileSaver, "saveAs").mockImplementation(() => {return; });

    agareService = MockService(AgareService, {
      addAgare: jest.fn((_projektId, _fastighetId, ag) => of(ag)),
      deleteAgare: jest.fn(_ => of(void 0)),
      editAgare: jest.fn(agare => of(agare)),
      setInkluderaIAvtal: jest.fn().mockReturnValue(of(void 0))
    });

    avtalService = MockService(AvtalService, {
      getAvtal: jest.fn((_projektId, _fastighetId) => of(avtal)),
      getAvtalstatus: jest.fn((_projektId, _fastighetId) => of(Avtalsstatus.EJBEHANDLAT)),
      setAvtalstatus: jest.fn((_projektId, _fastighetId, _status) => of(void 0)),
      getGeometristatusMessage: jest.fn(_ => MESSAGE),
      getLoggbok$: jest.fn((_projektId, _fastighetId) => of([])),
      setSkogsfastighet: jest.fn().mockReturnValue(of(undefined)),
      setTillvaratagandeTyp: jest.fn().mockReturnValue(of(undefined))
    });

    dialogService = MockService(DialogService, {
      deleteOmbudDialog: jest.fn(() => of(true)),
      confirmAvtalstatusDialog: jest.fn(() => of(true)),
      confirmErsattningUtbetaldDialog: jest.fn(() => of(true))
    });

    notificationService = MockService(XpNotificationService, {
      success: jest.fn(),
      error: jest.fn()
    });

    translation = MockService(TranslocoService, {
      translate: <T>(key, _params, _lang) => key
    });

    fastighetService = MockService(FastighetService, {
      resetGeometristatus: jest.fn((_projektId, _fastighetId) => of(Geometristatus.OFORANDRAD)),
      removeFastighet: jest.fn((_projektId, _fastighetId) => of(void 0)),
      setFastighetProjektInfo: jest.fn((_fastighetId, _projektId, info) => of(1)),
      fastighetsinformation: jest.fn().mockReturnValue(of(fastighetsinfo))
    });

    dokumentService = MockService(DokumentService, {
      getAvtalPDF: jest.fn((_projektId, _fastighetId) => of({name: "filnamn", blob: new Blob()})),
      getVarderingSkogsmarkXlsx: jest.fn().mockReturnValue(of(undefined))
    });

    varderingsprotokollService = MockService(ElnatVarderingsprotokollService, {
      getVarderingsprotokoll: jest.fn(),
      getVarderingsprotokollWithAvtalId: jest.fn().mockReturnValue(of(vp))
    });

    fiberVarderingsprotokollService = MockService(MkFiberVarderingsprotokollService, {
      getFiberVarderingsprotokollWithAvtalId: jest.fn().mockReturnValue(of(vp))
    });

    projektService = MockService(ProjektService, {
      getFiberProjekt: jest.fn().mockReturnValue(of(fiberProjekt)),
      getElnatProjekt: jest.fn().mockReturnValue(of(elnatProjekt))
    });

    errorService = MockService(XpErrorService, {
      handleBlobError: jest.fn()
    });

    projektkartaService = MockService(MkProjektkartaService);

    container = new MkRegisterenhetContainerConcrete(activatedRoute, agareService, avtalService,
      dialogService, dokumentService, fastighetService, notificationService, translation,
      projektService, varderingsprotokollService, fiberVarderingsprotokollService, errorService,
      projektkartaService);

    container.projektId = "projektId";
    container.projektTyp = ProjektTyp.FIBER;

    container.ngOnChanges(null);
  });

  it("ska hämta avtal vid initialisering", () => {
    expect(container.avtal).toBe(avtal);
  });

  it(`ska anropa ${AvtalService.name} för att hämta avtal vid initialisering`, () => {
    expect(avtalService.getAvtal).toHaveBeenCalledWith(container.projektId, container.fastighetId);
  });

  it("Ska initialisera versionsmeddelande med avseende på geometristatusen", () => {
    expect(container.versionMessage).toEqual(MESSAGE);
  });

  it("Ska uppdatera avtal när man lägger till ett ombud", () => {
    // Given
    container.avtal.ombud = [];

    // When
    container.onOmbudCreate(ombud);

    // Then
    expect(container.avtal.ombud).toEqual([ombud]);
  });

  it(`ska anropa ${AgareService.name} när man lägger till ett ombud`, () => {
    // Given
    container.avtal.ombud = [];

    // When
    container.onOmbudCreate(ombud);

    // Then
    expect(agareService.addAgare).toHaveBeenCalledWith(container.projektId, container.fastighetId, ombud);
  });

  it("Ska notifiera när ett ombud är tillagt", () => {
    // Given
    container.avtal.ombud = [];

    // When
    container.onOmbudCreate(ombud);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.agareinformation.ombudAdded");
  });

  it("Ska uppdatera avtal när man tar bort ett ombud", () => {
    // Given
    container.avtal.ombud = [ombud, ombud2];

    // When
    container.onOmbudDelete(ombud);

    // Then
    expect(container.avtal.ombud).toEqual([ombud2]);
  });

  it(`ska anropa ${AgareService.name} när man tar bort ett ombud`, () => {
    // When
    container.onOmbudDelete(ombud);

    // Then
    expect(agareService.deleteAgare).toHaveBeenCalledWith(ombud);
  });

  it("Ska notifiera när ett ombud är borttaget", () => {
    // When
    container.onOmbudDelete(ombud);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.agareinformation.ombudDeleted");
  });

  it("Ska uppdatera avtal när man redigerar en markägare", () => {
    // Given
    container.avtal.lagfarnaAgare = [testMarkagare()];
    const agareEdited = testMarkagare();
    agareEdited.adress = "en ny adress";
    expect(agareEdited.adress).not.toEqual(testMarkagare().adress);

    // When
    container.onAgareChange(agareEdited);

    // Then
    expect(container.avtal.lagfarnaAgare).toEqual([agareEdited]);
  });

  it(`ska anropa ${AgareService.name} när man redigerar en markägare`, () => {
    // Given
    const agare = testMarkagare();

    // When
    container.onAgareChange(agare);

    // Then
    expect(agareService.editAgare).toHaveBeenCalledWith(agare);
  });

  it("Ska notifiera när en markägare är redigerad", () => {
    // When
    container.onAgareChange(testMarkagare());

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("xp.common.saved");
  });

  it("Ska uppdatera avtalsstatusen när statusen för en markägare uppdateras", () => {
    // Given
    const status = Avtalsstatus.AVTALSIGNERAT;
    avtalService.getAvtalstatus = jest.fn((_projektId, _fastighetId) => of(status));
    expect(container.avtal.status).not.toEqual(status);

    // When
    container.onAgareChange(testMarkagare());

    // Then
    expect(container.avtal.status).toEqual(status);
  });

  it("Ska öppna dialogruta när avtalsstatus ändras", () => {
    // Given
    const status = Avtalsstatus.AVTALSKICKAT;

    // When
    container.onAvtalStatusChange(status);

    // Then
    expect(dialogService.confirmAvtalstatusDialog).toHaveBeenCalled();
  });

  it("Ska uppdatera avtalsstatusen för ett avtal korrekt", () => {
    // Given
    const status = Avtalsstatus.AVTALSKICKAT;
    expect(status).not.toEqual(container.avtal.status);

    container.avtal.lagfarnaAgare = [testMarkagare()];
    container.avtal.tomtrattsinnehavare = [testMarkagare()];
    container.avtal.ombud = [ombud];

    // When
    container.onAvtalStatusChange(status);

    // Then
    expect(container.avtal.status).toEqual(status);
    expect(container.avtal.lagfarnaAgare.map(ag => ag.status)).toEqual([status]);
    expect(container.avtal.tomtrattsinnehavare.map(ag => ag.status)).toEqual([status]);
    expect(container.avtal.ombud.map(ag => ag.status)).toEqual([status]);
  });

  it(`Ska anropa ${AvtalService.name} för att uppdatera avtalsstatusen`, () => {
    // Given
    const status = Avtalsstatus.AVTALSKICKAT;
    expect(status).not.toEqual(container.avtal.status);

    container.avtal.lagfarnaAgare = [testMarkagare()];
    container.avtal.tomtrattsinnehavare = [testMarkagare()];
    container.avtal.ombud = [ombud];

    // When
    container.onAvtalStatusChange(status);

    // Then
    expect(avtalService.setAvtalstatus).toHaveBeenCalledWith(container.projektId, container.fastighetId, status);
  });

  it("Ska öppna dialogruta när avtalsstatus ändras till \"ersättning utbetald\"", () => {
    // Given
    const status = Avtalsstatus.ERSATTNINGUTBETALD;

    // When
    container.onAvtalStatusChange(status);

    // Then
    expect(dialogService.confirmErsattningUtbetaldDialog).toHaveBeenCalled();
  });

  it("Ska notifiera när avtalsstatusen är uppdaterad", () => {
    // Given
    const status = Avtalsstatus.AVTALSKICKAT;
    expect(status).not.toEqual(container.avtal.status);

    container.avtal.lagfarnaAgare = [testMarkagare()];
    container.avtal.tomtrattsinnehavare = [testMarkagare()];
    container.avtal.ombud = [ombud];

    // When
    container.onAvtalStatusChange(status);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.fastighetsinformation.statusSaved");
  });

  it(`Ska anropa ${FastighetService.name} om ändringar bekräftas för en ny fastighet`, () => {
    // Given
    container.avtal.geometristatus = Geometristatus.NY;
    container.ngOnInit();

    // When
    container.onConfirmVersionChange();

    // Then
    expect(fastighetService.resetGeometristatus).toHaveBeenCalledWith(container.projektId, container.fastighetId);
  });

  it(`Ska anropa ${FastighetService.name} om ändringar bekräftas för en uppdaterad fastighet`, () => {
    // Given
    container.avtal.geometristatus = Geometristatus.UPPDATERAD;
    container.ngOnInit();

    // When
    container.onConfirmVersionChange();

    // Then
    expect(fastighetService.resetGeometristatus).toHaveBeenCalledWith(container.projektId, container.fastighetId);
  });

  it(`Ska anropa ${FastighetService.name} om ändringar bekräftas för en borttagen fastighet`, () => {
    // Given
    container.avtal.geometristatus = Geometristatus.BORTTAGEN;
    container.ngOnInit();

    // When
    container.onConfirmVersionChange();

    // Then
    expect(fastighetService.removeFastighet).toHaveBeenCalledWith(container.projektId, container.fastighetId);
  });

  it(`Ska anropa ${AvtalService.name} för att generera avtal`, () => {
    // When
    container.onSkapaAvtalClick();

    // Then
    expect(dokumentService.getAvtalPDF).toHaveBeenCalledWith(container.projektId, container.fastighetId);
  });

  it("Ska notifiera när avtalet är genererat", () => {
    // When
    container.onSkapaAvtalClick();

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.fastighetsinformation.avtalGenerated");
  });

  it("Ska notifiera om något gick fel vid avtalsgenerering", () => {
    // Given
    dokumentService.getAvtalPDF = throwError$;

    // When
    container.onSkapaAvtalClick();

    // Then
    expect(errorService.handleBlobError).toHaveBeenCalledWith(ERROR);
  });

  it(`Ska anropa ${FastighetService.name} för att uppdatera anteckningar`, () => {
    // Given
    const anteckning = "anteckning";
    container.avtal.ersattning = 123;
    const info: FastighetsProjektInfo = {
      fastighetsId: container.fastighetId,
      projektId: container.projektId,
      ersattning: container.avtal.ersattning,
      anteckning: anteckning
    };

    // When
    container.onAnteckningarChange(anteckning);

    // Then
    expect(fastighetService.setFastighetProjektInfo).toHaveBeenCalledWith(
      container.fastighetId, container.projektId, info
    );
  });

  it("Ska notifiera när anteckningar har uppdaterats", () => {
    // Given
    const anteckning = "anteckning";
    container.avtal.ersattning = 123;

    // When
    container.onAnteckningarChange(anteckning);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("xp.common.saved");
  });

  it("Ska notifiera om anteckningar inte gick att uppdatera", () => {
    // Given
    fastighetService.setFastighetProjektInfo = throwError$;

    const anteckning = "anteckning";
    container.avtal.ersattning = 123;

    // When
    container.onAnteckningarChange(anteckning);

    // Then
    expect(notificationService.error).toHaveBeenCalledWith(ERROR);
  });

  it(`Ska anropa ${FastighetService.name} för att uppdatera ersättning`, () => {
    // Given
    const ersattning = 123;
    container.avtal.anteckning = "anteckning";
    container.avtal.ersattning = 123;
    const info: FastighetsProjektInfo = {
      fastighetsId: container.fastighetId,
      projektId: container.projektId,
      ersattning: ersattning,
      anteckning: container.avtal.anteckning
    };

    // When
    container.onIntrangChange(ersattning);

    // Then
    expect(fastighetService.setFastighetProjektInfo).toHaveBeenCalledWith(
      container.fastighetId, container.projektId, info
    );
  });

  it("Ska notifiera när ersättning har uppdaterats", () => {
    // Given
    const anteckning = "anteckning";
    const ersattning = 123;
    container.avtal.anteckning = "anteckning";
    container.avtal.ersattning = 123;

    // When
    container.onIntrangChange(ersattning);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("xp.common.saved");
  });

  it("Ska notifiera om ersättning inte gick att uppdatera", () => {
    // Given
    fastighetService.setFastighetProjektInfo = throwError$;

    const ersattning = 123;
    container.avtal.anteckning = "anteckning";
    container.avtal.ersattning = 123;

    // When
    container.onIntrangChange(ersattning);

    // Then
    expect(notificationService.error).toHaveBeenCalledWith(ERROR);
  });

  it(`Ska anropa ${AgareService.name} för att sätta signera avtal`, () => {
    // Given
    const signera = true;
    container.avtal.lagfarnaAgare.forEach(ag => ag.inkluderaIAvtal = !signera);
    container.avtal.tomtrattsinnehavare.forEach(ag => ag.inkluderaIAvtal = !signera);
    container.avtal.ombud.forEach(ag => ag.inkluderaIAvtal = !signera);

    const agareExpect = [
      ...container.avtal.lagfarnaAgare,
      ...container.avtal.tomtrattsinnehavare,
      ...container.avtal.ombud
    ];

    // When
    container.signAvtalCheckAllChange(signera);

    // Then
    expect(agareService.setInkluderaIAvtal).toHaveBeenCalledWith(agareExpect, signera);
  });

  it("Ska notifiera när inkludering i avtal är satt", () => {
    // When
    container.signAvtalCheckAllChange(true);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("xp.common.saved");
  });

  it("Ska uppdatera ägarna när signera är satt", () => {
    // Given
    const signera = true;
    container.avtal.lagfarnaAgare.forEach(ag => ag.inkluderaIAvtal = !signera);
    container.avtal.tomtrattsinnehavare.forEach(ag => ag.inkluderaIAvtal = !signera);
    container.avtal.ombud.forEach(ag => ag.inkluderaIAvtal = !signera);

    // When
    container.signAvtalCheckAllChange(signera);

    // Then
    avtal.lagfarnaAgare.forEach(ag => expect(ag.inkluderaIAvtal).toEqual(signera));
    avtal.tomtrattsinnehavare.forEach(ag => expect(ag.inkluderaIAvtal).toEqual(signera));
    avtal.ombud.forEach(ag => expect(ag.inkluderaIAvtal).toEqual(signera));
  });

  it("ska anropa DokumentService när man exporterar Exceldokument", () => {
    // When
    container.onExportXlsxClick();

    // Then
    expect(dokumentService.getVarderingSkogsmarkXlsx).toHaveBeenCalledWith(container.projektId,
      container.fastighetId);
  });

  it("ska spara ner till fil vid export av Exceldokument", () => {
    // Given
    const file = { blob: "blob", name: "name"};
    dokumentService.getVarderingSkogsmarkXlsx = jest.fn().mockReturnValue(of(file));

    // When
    container.onExportXlsxClick();

    // Then
    expect(FileSaver.saveAs).toHaveBeenCalledWith(file.blob, file.name);
  });
});
