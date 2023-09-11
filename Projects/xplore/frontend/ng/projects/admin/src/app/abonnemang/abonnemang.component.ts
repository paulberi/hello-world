import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { TranslocoService } from "@ngneat/transloco";
import { Abonnemang } from "../../../../../generated/kundconfig-api";
import { AdmAbonnemangPresenter } from "./abonnemang.presenter";

@Component({
  providers: [AdmAbonnemangPresenter],
  selector: "adm-abonnemang-ui",
  templateUrl: "./abonnemang.component.html",
  styleUrls: ["./abonnemang.component.scss"]
})
export class AdmAbonnemangComponent implements OnInit, OnChanges {
  @Input() abonnemang: Abonnemang[] = [];
  @Input() produkter: String[] = [];
  @Input() abonnemangTyper: String[] = [];
  @Input() showForm = false;

  @Output() abonnemangAdd = this.presenter.abonnemangAdd;
  @Output() abonnemangDelete = new EventEmitter<Abonnemang>();
  @Output() showFormChange = new EventEmitter<boolean>();

  readonly columns = ["edit", "produkt", "nivå", "delete"];

  constructor(private presenter: AdmAbonnemangPresenter,
              private translate: TranslocoService) {}

  ngOnInit(): void {
    this.presenter.initializeForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.showForm && this.showForm) {
      this.presenter.initializeForm();
    }
  }

  hasAbonnemang(): boolean {
    return this.abonnemang?.length > 0;
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  toggleForm() {
    this.showFormChange.emit(!this.showForm);
  }

  submit() {
    this.presenter.submit();
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }
}
