import { Component, EventEmitter, Input, Output } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { ElnatInfo, ElnatProjekt, FiberInfo, FiberProjekt, Ledningsagare, NisKalla, ProjektInfo, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { UserWithRoll } from "../projekt-behorighet/projekt-behorighet.container";
import { MkUploadFile } from "../upload-file/upload-file.presenter";

export interface MkFiberProjekt {
  projekt: FiberProjekt;
  file: File;
  users: UserWithRoll[];
}

export interface MkElnatProjekt {
  projekt: ElnatProjekt;
  file: File;
  users: UserWithRoll[];
}

/**
 * Skapa ett nytt projekt i Markkoll, antingen med projekttypen Fiber eller Elnät.
 */
@Component({
  selector: "mk-create-projekt",
  templateUrl: "./create-projekt.component.html",
  styleUrls: ["./create-projekt.component.scss"]
})

export class MkCreateProjektComponent {

  public projektInfo: ProjektInfo;
  public fiberInfo: FiberInfo;
  public elnatInfo: ElnatInfo;
  public uploadFile: MkUploadFile;
  public usersWithNewRoll: UserWithRoll[];

  public isProjektInfoValid: boolean;
  public isUploadFileValid = true;
  public selectedProjekttyp: ProjektTyp = null;

  @Input() isCreatingProjekt = false;

  @Input() ledningsagare: string[] = [];

  @Input() nisKalla: NisKalla;

  @Output() create =  new EventEmitter<MkFiberProjekt | MkElnatProjekt>();

  constructor(private translate: TranslocoService) { }

  createProjekt() {
    let projekt: MkFiberProjekt | MkElnatProjekt;
    if (this.isCreatingProjekt) {
      return;
    }
    if (this.isFiberProjekt) {
      projekt = {
        projekt: {
          projektInfo: this.projektInfo,
          fiberInfo: this.fiberInfo,
          indataTyp: this.uploadFile?.indataTyp,
          buffert: this.uploadFile?.buffert
        },
        file: this.uploadFile?.files[0],
        users: this.usersWithNewRoll
      } as MkFiberProjekt;
    } else if (this.isElnatProjekt) {
      projekt = {
        projekt: {
          projektInfo: this.projektInfo,
          elnatInfo: this.elnatInfo,
          indataTyp: this.uploadFile?.indataTyp,
          buffert: this.uploadFile?.buffert
        },
        file: this.uploadFile?.files[0],
        users: this.usersWithNewRoll
      } as MkElnatProjekt;
    }
    this.create.emit(projekt);
  }

  updateProjektInfo(projektInfo: ProjektInfo) {
    this.projektInfo = projektInfo;
    this.selectedProjekttyp = projektInfo.projektTyp;
  }

  updateFiberInfo(fiberInfo: FiberInfo) {
    this.fiberInfo = fiberInfo;
  }

  updateElnatInfo(elnatInfo: ElnatInfo) {
    this.elnatInfo = elnatInfo;
  }

  updateUploadFile(uploadFile: MkUploadFile) {
    this.uploadFile = uploadFile;
  }

  updateUserSelection(userWithNewRoll: UserWithRoll[]) {
    this.usersWithNewRoll = userWithNewRoll;
  }

  get isFiberProjekt(): boolean {
    return this.projektInfo?.projektTyp === ProjektTyp.FIBER;
  }

  get isElnatProjekt(): boolean {
    return (this.projektInfo?.projektTyp === ProjektTyp.LOKALNAT) ||
        (this.projektInfo?.projektTyp === ProjektTyp.REGIONNAT);
  }
}
